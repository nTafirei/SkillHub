package com.marotech.recording.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marotech.recording.api.HttpCode;
import com.marotech.recording.api.RecordingDTO;
import com.marotech.recording.api.ResponseType;
import com.marotech.recording.api.ServiceResponse;
import com.marotech.recording.config.Config;
import com.marotech.recording.model.*;
import com.marotech.recording.service.RepositoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private Config config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(RecordController.class);

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Saves a recording in the system", notes = "Saves a recording in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recording has been saved"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> saveRecording(
            @RequestParam("metadata") String metadataJson,
            @RequestParam("imageData") String imageDataBase64,
            @RequestParam("video") MultipartFile videoFile) {

        try {

            if (!"video/mp4".equals(videoFile.getContentType())) {
                ServiceResponse response = new ServiceResponse();
                response.setCode(HttpCode.BAD_REQUEST);
                response.setResponseType(ResponseType.RECORDINGS);
                response.setMessage("An invalid conte type was found. Please try with mp4");
                LOG.error("An invalid conte type was found: " + metadataJson);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 1. Parse metadata JSON
            RecordingDTO dto = objectMapper.readValue(metadataJson, RecordingDTO.class);
            String uploadDir = config.getProperty("app.media.storage.path");

/*            // 2. Save video file
            Path videoPath = Paths.get(uploadDir + dto.getName());
            Files.createDirectories(videoPath.getParent());
            videoFile.transferTo(videoPath);*/

            String token = dto.getToken();
            User user = null;
            if (StringUtils.isNotBlank(token)) {
                AppSession session = repositoryService.fetchAppSessionByToken(token);
                String mobileNumber = session.getMobileNumber();
                AuthUser authUser = repositoryService.fetchAuthUserByMobileNumber(mobileNumber);
                user = authUser.getUser();
            }
            Recording recording = new Recording();
            recording.setName(dto.getName());
            recording.setMobileNumber(dto.getMobileNumber());
            recording.setDeviceLocation(dto.getDeviceLocation());
            repositoryService.save(recording);

            dto.setId(recording.getId());

            //Decode and save thumbnail (320x240)
            byte[] imageBytes = Base64.getDecoder().decode(imageDataBase64);
            Path thumbPath = Paths.get(uploadDir + "/" + recording.getId() + "_" + recording.getName() + ".jpg");
            Files.write(thumbPath, imageBytes);
            ServiceResponse response = new ServiceResponse();
            response.setResponseType(ResponseType.RECORDINGS);
            log.info("Recording uploaded: {} ({} bytes)", recording.getId(), videoFile.getSize());

            try (InputStream videoStream = videoFile.getInputStream()) {
                // Save video to disk
                Path targetPath = Paths.get(uploadDir + "/" + recording.getId() + "_" + recording.getName());
                Files.copy(videoStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            List<RecordingDTO> dtos = new ArrayList<>();
            dtos.add(dto);
            response.getAdditionalInfo().put(RECORDINGS, GSON.toJson(dtos));
            createActivityLog(metadataJson
                    + " saved recording using web service");

            return ResponseEntity.ok(dtos);
        } catch (Exception ex) {
            ServiceResponse response = new ServiceResponse();
            response.setCode(HttpCode.BAD_REQUEST);
            response.setResponseType(ResponseType.RECORDINGS);
            response.setMessage("An error occurred saving the recording. Please contact customer service team");
            LOG.error("Error saving recording for " + metadataJson, ex);
            createActivityLog(metadataJson
                    + " failed to save recording using web service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private void createActivityLog(String message) {
        if (!shouldAudit()) {
            return;
        }
        Activity activity = new Activity();
/*        if (authUser != null) {
            activity.setActor(authUser.getUser());
        } else {
            AuthUser user = repositoryService.fetchAuthUserByMobileNumber(mobileNumber);
            if (user != null) {
                activity.setActor(user.getUser());
            }
        }*/
        activity.setActivityType(ActivityType.UPLOADED_RECORDING);
        activity.setTitle(message);
        repositoryService.save(activity);
    }

    private static final Logger LOG = LoggerFactory.getLogger(RecordController.class);
}
