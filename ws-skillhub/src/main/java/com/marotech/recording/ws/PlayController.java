package com.marotech.recording.ws;

import com.marotech.recording.api.ServiceResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/play")
public class PlayController {

    @Autowired
    private AudioStreamHandler audioHandler;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Plays a recording from the system",
            notes = "Plays a recording from the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recording is played"),
            @ApiResponse(code = 400, message = "Bad request. Adjust values before retrying again", response =
                    ServiceResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })
    public ResponseEntity<String> playRecording(
            @RequestParam String mobileNumber,
            @RequestParam String fileName,
            HttpServletRequest request) {

        WebSocketSession session = audioHandler.getSession(mobileNumber);
        if (session == null || !session.isOpen()) {
            return ResponseEntity.badRequest()
                    .body("No active WebSocket session for mobile: " + mobileNumber);
        }

        String clientIp = getClientIpAddress(request);
        if (mobileNumber == null || fileName == null || !Files.exists(Paths.get(fileName))) {
            return ResponseEntity.badRequest().body(String.format("Invalid mobile or file not found for IP : {}",
                    clientIp));
        }
        LOG.info("Playback request from IP: {} for mobile: {}", clientIp, mobileNumber);
        // Non-blocking playback
        CompletableFuture.runAsync(() ->
                audioHandler.playBackAudio(mobileNumber, fileName));

        return ResponseEntity.ok("Playback started for " + mobileNumber);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        // Check forwarded headers first (proxy/load balancer)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // May contain multiple IPs, take first one
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // Fallback to direct remote address
        return request.getRemoteAddr();
    }

    @GetMapping("/session/{mobileNumber}")
    public ResponseEntity<String> getSessionStatus(@PathVariable String mobileNumber) {
        try {
            audioHandler.getSession(mobileNumber);
            boolean isActive = audioHandler.getSession(mobileNumber) != null
                    && audioHandler.getSession(mobileNumber).isOpen();

            return ResponseEntity.ok(isActive ? "Session active" : "No active session");
        } catch (Exception e) {
            LOG.error("Error checking session status for mobile: {}", mobileNumber, e);
            return ResponseEntity.ok("Session status unavailable");
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(PlayController.class);
}
