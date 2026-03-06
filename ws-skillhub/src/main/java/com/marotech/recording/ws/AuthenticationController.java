package com.marotech.recording.ws;

import com.marotech.recording.api.OTPVerificationRequest;
import com.marotech.recording.api.PasswordVerificationRequest;
import com.marotech.recording.api.HttpCode;
import com.marotech.recording.api.ResponseType;
import com.marotech.recording.api.ServiceResponse;
import com.marotech.recording.model.Activity;
import com.marotech.recording.model.ActivityType;
import com.marotech.recording.model.AppSession;
import com.marotech.recording.model.AuthUser;
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

import java.time.LocalDateTime;


@RestController
@RequestMapping("/authenticate")
public class AuthenticationController extends BaseController{

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping(value = "/verifyMobileAndPassword", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Attempts to log in a user using their mobile number and password",
            notes = "Verifies mobile number and password against the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mobile number and password have benn verified"),
            @ApiResponse(code = 400, message = "Bad request. Adjust values before retrying again", response =
                    ServiceResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })

    public ResponseEntity<ServiceResponse> verifyMobileAndPassword(@RequestBody
                                                                   PasswordVerificationRequest request) {
        ServiceResponse response = new ServiceResponse();
        response.setResponseType(ResponseType.AUTH_RESPONSE);

        try {
            if (request == null || !request.isValid()) {
                response.setCode(HttpCode.BAD_REQUEST);
                LOG.error("Error: {} ", NULL_REQUEST_FOUND);
                response.setMessage("Either mobile number or password was not specified");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            AuthUser authUser = repositoryService.fetchAuthUserByMobileNumber(request.getMobileNumber());
            if (authUser == null) {
                response.setCode(HttpCode.UN_AUTHORISED);
                response.setMessage("Specified account was not found in the system. Please register first");
                LOG.error("Error: Specified account was not found in the system. User must register first: " + request);
                createActivityLog(request.getMobileNumber(), null,
                        request.getMobileNumber() + " failed to log in online on using web service");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            try {
                String newPassword = AuthUser.encodedPassword(request.getPassword());
                if (!newPassword.equals(authUser.getPassword())) {
                    response.setCode(HttpCode.BAD_REQUEST);
                    response.setMessage("Invalid mobile number or password found");
                    createActivityLog(request.getMobileNumber(), null, request.getMobileNumber() + " failed to log in online on using web services");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                AppSession appSession = new AppSession();
                appSession.setMobileNumber(request.getMobileNumber());
                repositoryService.save(appSession);
                response.setToken(appSession.getToken());
                response.getAdditionalInfo().put("ttl", config.getProperty(APP_SESSION_TTL));
                createActivityLog(request.getMobileNumber(), authUser,
                        request.getMobileNumber() + " logged in online on using web service");
                LOG.error("Returning : " + response);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (Exception ex) {
                response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
                response.setMessage("An error occurred in the system. Please contact customer service team");
                LOG.error("Error encoding password for " + request.getMobileNumber(), ex);
                createActivityLog(request.getMobileNumber(), authUser, request.getMobileNumber()
                        + " failed to log in online on using web service");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception ex) {
            response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
            response.setMessage("An error occurred in the system. Please contact our customer service");
            LOG.error("Error", ex);
            createActivityLog(request.getMobileNumber(), null,
                    request.getMobileNumber() + " failed to log in online on using web service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/verifyMobileAndOneTimePasscode", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Verify mobile number and one time passcode",
            notes = "Verifies mobile number and a one time passcode")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mobile number and one time passcode have been verified"),
            @ApiResponse(code = 400, message = "Bad request. Adjust values before retrying again", response =
                    ServiceResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })

    public ResponseEntity<ServiceResponse>
    verifyMobileAndOneTimePasscode(@RequestBody OTPVerificationRequest request) {
        ServiceResponse response = new ServiceResponse();
        response.setResponseType(ResponseType.AUTH_RESPONSE);
        try {
            if (request == null || !request.isValid()) {
                response.setCode(HttpCode.BAD_REQUEST);
                response.setMessage("Either mobile number or password was not specified");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            try {
                AppSession appSession = repositoryService.
                        fetchAppSessionByMobileNumberAndOTP(request.getMobileNumber(), request.getOtp());
                if (appSession == null) {
                    response.setCode(HttpCode.BAD_REQUEST);
                    response.setMessage("Specified otp and mobile number were not found in our system");
                    createActivityLog(request.getMobileNumber(), null, request.getMobileNumber() + " failed to log in online on using web services");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                LocalDateTime updatedTime = appSession.getDateCreated();
                LocalDateTime expiryTime = updatedTime.plusSeconds(config.getIntegerProperty("app.otp.ttl"));
                if (LocalDateTime.now().isAfter(expiryTime)) {
                    response.setCode(HttpCode.BAD_REQUEST);
                    createActivityLog(request.getMobileNumber(), null, request.getMobileNumber() + " failed to log in online on using web services");
                    response.setMessage("Specified otp has expired. Please contact our customer service team");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                response.setToken(appSession.getToken());
                response.getAdditionalInfo().put(TTL, config.getProperty(APP_SESSION_TTL));
                createActivityLog(request.getMobileNumber(), null, request.getMobileNumber() + " logged in online on using web services");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (Exception ex) {
                response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
                response.setMessage("An error occurred in the system. Please contact customer service team");
                LOG.error("Error encoding password for " + request.getMobileNumber(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception ex) {
            response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
            response.setMessage("An error occurred in the system. Please contact our customer service team");
            LOG.error("Error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private void createActivityLog(String mobileNumber, AuthUser authUser, String message) {
        if(!shouldAudit()){
            return;
        }
        Activity activity = new Activity();
        if (authUser != null) {
            activity.setActor(authUser.getUser());
        } else {
            AuthUser user = repositoryService.fetchAuthUserByMobileNumber(mobileNumber);
            if (user != null) {
                activity.setActor(user.getUser());
            }
        }
        activity.setActivityType(ActivityType.LOGGED_IN_VIA_WEB_SERVICES);
        activity.setTitle(message);
        repositoryService.save(activity);
    }

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
}
