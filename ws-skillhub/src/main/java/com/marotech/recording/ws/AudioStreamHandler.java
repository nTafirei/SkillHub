package com.marotech.recording.ws;

import com.marotech.recording.config.Config;
import com.marotech.recording.model.*;
import com.marotech.recording.service.RepositoryService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AudioStreamHandler extends BinaryWebSocketHandler {

    @Autowired
    private Config config;
    @Autowired
    private RepositoryService repositoryService;

    private final Map<String, ByteArrayOutputStream> buffers = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private String saveDir;

    @PostConstruct
    public void setParameters() {
        saveDir = config.getProperty("app.audio.storage.path");
        LOG.info("Audio storage directory set to: {}", saveDir);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String mobileNumber = getHeader("Mobile-Number",session);
        if (mobileNumber != null) {
            sessions.put(mobileNumber, session);
            LOG.info("Session established for mobile: {}", mobileNumber);
        } else {
            LOG.warn("No mobile number found in headers for session: {}", session.getId());
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        String sessionId = session.getId();
        buffers.computeIfAbsent(sessionId, k -> new ByteArrayOutputStream())
                .write(message.getPayload().array(), 0, message.getPayload().array().length);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        ByteArrayOutputStream baos = buffers.remove(sessionId);

        // Remove from sessions map too
        sessions.entrySet().removeIf(entry -> entry.getValue().getId().equals(sessionId));

        if (baos != null) {
            String mobileNumber = getHeader("Mobile-Number",session);
            String fileName = getHeader("File-Name",session);
            String contentType = getHeader("Content-Type",session);
            String location = getHeader("Device-Location",session);
            String filePath = saveDir + File.separator + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                baos.writeTo(fos);
                LOG.info("Audio saved to: {}", filePath);
            } catch (IOException e) {
                LOG.error("Failed to save audio for session {} to {}", sessionId, filePath, e);
            }
            AuthUser authUser = repositoryService.fetchAuthUserByMobileNumber(mobileNumber);

            Recording recording = new Recording();
            recording.setName(fileName);
            recording.setMediaType(contentType);
            recording.setMobileNumber(mobileNumber);
            recording.setDeviceLocation(location);
            repositoryService.save(recording);

            if (shouldAudit()) {
                Activity activity = new Activity();
                if(authUser != null) {
                    User user = authUser.getUser();
                    activity.setActor(user);
                    activity.setTitle(user.getFullName() + " uploaded " + fileName + " on " + LocalDate.now());

                }else{
                    activity.setTitle(mobileNumber + " uploaded " + fileName + " on " + LocalDate.now());
                }
                activity.setActivityType(ActivityType.UPLOADED_RECORDING);
                activity.setTitle(recording.getName());
                repositoryService.save(activity);
            }
        }
        super.afterConnectionClosed(session, status);
    }
    public List<String> getContentTypes(){
        String str = config.getProperty("app.content.types");
        String[] array = str.split(",");
        return Arrays.asList(array);
    }
    private String getHeader(String name, WebSocketSession session) {
        try {
            return session.getHandshakeHeaders().get(name) != null
                    ? session.getHandshakeHeaders().get(name).get(0)
                    : null;
        } catch (Exception e) {
            LOG.warn("Error extracting mobile number from headers", e);
            return null;
        }
    }
    protected boolean shouldAudit() {
        return config.getBooleanProperty("app.should.audit");
    }
    public void playBackAudio(String mobileNumber, String fileName) {
        WebSocketSession session = sessions.get(mobileNumber);
        if (session != null && session.isOpen()) {
            try (InputStream inputStream = new FileInputStream(fileName)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                    BinaryMessage message = new BinaryMessage(chunk);
                    session.sendMessage(message);
                    Thread.sleep(20); // Pace playback
                }
                LOG.info("Playback completed for mobile: {}", mobileNumber);
            } catch (IOException | InterruptedException e) {
                LOG.error("Failed to stream audio playback for file: {}", fileName, e);
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            LOG.warn("No active session found for mobile number: {}", mobileNumber);
        }
    }

    public WebSocketSession getSession(String mobileNumber) {
        return sessions.get(mobileNumber);
    }
    private static final Logger LOG = LoggerFactory.getLogger(AudioStreamHandler.class);
}
