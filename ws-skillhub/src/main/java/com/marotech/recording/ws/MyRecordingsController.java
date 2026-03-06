package com.marotech.recording.ws;


import com.marotech.recording.api.*;
import com.marotech.recording.model.AppSession;
import com.marotech.recording.model.AuthUser;
import com.marotech.recording.model.Recording;
import com.marotech.recording.model.User;
import com.marotech.recording.service.RepositoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/my-recordings")
public class MyRecordingsController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Fetches recordings from system",
            notes = "Fetches recordings from system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Token is valid. Recordings list is included"),
            @ApiResponse(code = 400, message = "Bad request. Adjust values before retrying again", response =
                    ServiceResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })
    public ResponseEntity<ServiceResponse>
    getMyRecordings(@RequestBody RecordingsRequest request) {
        ServiceResponse response = new ServiceResponse();
        response.setResponseType(ResponseType.RECORDINGS);
        try {
            if (request == null) {
                response.setCode(HttpCode.BAD_REQUEST);
                response.setMessage(NULL_REQUEST_FOUND);
                LOG.error("Error: {} ", NULL_REQUEST_FOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            validateRequest(request, response);
            if (response.getCode() != HttpCode.OK) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            AppSession appSession = repositoryService.
                    fetchAppSessionByToken(request.getToken());
            if (appSession == null) {
                response.setCode(HttpCode.BAD_REQUEST);
                LOG.error("Error: {} {}", TOKEN_NOT_FOUND, request);
                response.setMessage(TOKEN_NOT_FOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            LocalDateTime updatedTime = appSession.getDateLastUpdated();
            LocalDateTime expiryTime = updatedTime.
                    plusSeconds(config.getIntegerProperty(APP_SESSION_TTL));
            if (LocalDateTime.now().isAfter(expiryTime)) {
                response.setCode(HttpCode.UN_AUTHORISED);
                response.setMessage("Specified token has expired. Please log in again");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            response.setToken(appSession.getToken());
            AuthUser authUser = repositoryService.fetchAuthUserByMobileNumber(appSession.getMobileNumber());
            User user = authUser.getUser();
            List<RecordingDTO> dtos = null;

            if (request.getStartDate() == null) {
                List<Recording> recordings = repositoryService.fetchRecordingsForUser(user, request.getPage());
                if (recordings.isEmpty()) {
                    response.setMessage(NO_VOUCHERS_FOUND);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                for (Recording recording : recordings) {
                    dtos = populateRecordingTOs(recording);
                }
            } else {
                List<Recording> recordings = repositoryService.fetchRecordingsForUser(user, request.getStartDate(),
                        request.getEndDate(), request.getPage());
                if (recordings.isEmpty()) {
                    response.setMessage(NO_VOUCHERS_FOUND);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                for (Recording recording : recordings) {
                    dtos = populateRecordingTOs(recording);
                }
            }

            response.getAdditionalInfo().put(RECORDINGS, GSON.toJson(dtos));
            appSession.setDateLastUpdated(LocalDateTime.now());
            repositoryService.save(appSession);
            response.setResponseType(ResponseType.RECORDINGS);
            response.getAdditionalInfo().put(TTL, config.getProperty(APP_SESSION_TTL));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
            response.setMessage("An error occurred in the system. Please contact our customer service");
            LOG.error("Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private List<RecordingDTO> populateRecordingTOs(Recording recording) {
        List<RecordingDTO> dtos = new ArrayList<>();
        RecordingDTO dto = new RecordingDTO();
        dto.setId(recording.getId());
        dto.setName(recording.getName());
        dto.setMimeType(recording.getMediaType());
        dto.setDeviceLocation(recording.getDeviceLocation());
        dtos.add(dto);
        return dtos;
    }

    private void validateRequest(RecordingsRequest request, ServiceResponse serviceResponse) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (startDate != null && endDate == null) {
            serviceResponse.setCode(HttpCode.BAD_REQUEST);
            LOG.error("Error: Both startDate and endDate must be specified: " + request);
            serviceResponse.setMessage("Both startDate and endDate must be specified");
            return;
        }
        if (endDate != null && startDate == null) {
            serviceResponse.setCode(HttpCode.BAD_REQUEST);
            LOG.error("Error: Both startDate and endDate must be specified: " + request);
            serviceResponse.setMessage("Both startDate and endDate must be specified");
            return;
        }
        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                serviceResponse.setCode(HttpCode.BAD_REQUEST);
                LOG.error("Error: endDate cannot be before startDate: " + request);
                serviceResponse.setMessage("endDate cannot be before startDate");
            }
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(MyRecordingsController.class);
}
