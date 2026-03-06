package com.marotech.recording.ws;

import com.marotech.recording.api.RegisterRequest;
import com.marotech.recording.api.HttpCode;
import com.marotech.recording.api.ResponseType;
import com.marotech.recording.api.ServiceResponse;
import com.marotech.recording.model.*;
import com.marotech.recording.service.RepositoryService;
import com.marotech.recording.util.Constants;
import com.marotech.recording.util.EmailValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/register")
public class RegistrationController extends BaseController{

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registers a user in the system",
            notes = "Registers a user in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registration successful"),
            @ApiResponse(code = 400, message = "Bad request. Adjust values before retrying again", response =
                    ServiceResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })
    public ResponseEntity<ServiceResponse>
    register(@RequestBody RegisterRequest request) {
        ServiceResponse response = new ServiceResponse();
        response.setResponseType(ResponseType.REG_RESPONSE);
        try {
            if (request == null) {
                response.setCode(HttpCode.BAD_REQUEST);
                response.setMessage(NULL_REQUEST_FOUND);
                LOG.error("Error: {} ", NULL_REQUEST_FOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            AuthUser authUser = repositoryService.fetchAuthUserByMobileNumber(request.getMobileNumber());
            if(authUser != null){
                response.setMessage("A user with that mobile number already exists in the system");
                LOG.error("Error: A user with that mobile number already exists in the system: " + request);
                response.setCode(HttpCode.BAD_REQUEST);
                createActivityLog(request.getMobileNumber(), null, request.getMobileNumber() + " tried to register using an already registered number via web services");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            StringBuilder builder = new StringBuilder();
            validateData(request, builder);
            if(builder.toString().length() > 0){
                response.setMessage(builder.toString());
                response.setCode(HttpCode.BAD_REQUEST);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            //----------------------------Create the AuthUser---------------------------
            authUser = createAuthUser(request);
            //-----------------------------Create the User-----------------------------
            createTheUser(request, authUser);
            //-------------------------------------------------------------------------

            response.getAdditionalInfo().put(RESPONSE_TYPE, REG_RESPONSE);
            response.getAdditionalInfo().put(STATUS, "You have been registered in our system. Please approach " +
                    "one of our agents for identity verification before you can use the system");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
            response.setMessage("An error occurred in the system. Please contact our customer service team");
            LOG.error("Error", e);
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
        activity.setActivityType(ActivityType.NEW_REGISTRATION_BY_MOBILE);
        activity.setTitle(message);
        repositoryService.save(activity);
    }

    private void createTheUser(RegisterRequest request, AuthUser authUser) {
        String securityQuestion1 = config.getProperty(SEC_QUESTION_1);
        String securityQuestion2 = config.getProperty(SEC_QUESTION_2);
        String securityQuestion3 = config.getProperty(SEC_QUESTION_3);
        Address theAddress = createUserAddress(request);

        User user = new User(request.getFirstName(),
                request.getMiddleName(), request.getLastName(), theAddress,
                request.getMobileNumber(), request.getNationalId());
        UserRole role = repositoryService.fetchUserRoleByRoleName("User");
        user.getUserRoles().add(role);
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setActiveStatus(ActiveStatus.NOT_ACTIVE);
        repositoryService.save(user);

        authUser.setUser(user);

        SecurityQuestion question = new SecurityQuestion();
        question.setQuestion(securityQuestion1);
        question.setAnswer(request.getAnswer1());
        question.setTag(SecurityQuestionTag.ONE);
        repositoryService.save(question);
        authUser.getSecurityQuestions().add(question);

        question = new SecurityQuestion();
        question.setQuestion(securityQuestion2);
        question.setAnswer(request.getAnswer2());
        question.setTag(SecurityQuestionTag.TWO);
        repositoryService.save(question);
        authUser.getSecurityQuestions().add(question);

        question = new SecurityQuestion();
        question.setQuestion(securityQuestion3);
        question.setAnswer(request.getAnswer3());
        question.setTag(SecurityQuestionTag.THREE);
        repositoryService.save(question);
        authUser.getSecurityQuestions().add(question);

        repositoryService.save(authUser);

        List<User> list = repositoryService.fetchAllUsersByRoleNames(Arrays.asList(ROLES));
        for (User staff : list) {
            Notification notification = new Notification();
            notification.setBody(config.getProperty(Constants.REG_BODY));
            notification.setFrom(request.getEmail());
            notification.setSubject(config.getProperty(Constants.REG_SUBJECT));
            notification.setRecipient(staff);
            repositoryService.save(notification);
        }

        if(shouldAudit()) {
            Activity activity = new Activity();
            activity.setActivityType(ActivityType.NEW_REGISTRATION_BY_MOBILE);
            activity.setActor(user);
            activity.setTitle(user.getFullName() + " registered on line on " + LocalDate.now());
            repositoryService.save(activity);
        }
    }

    private Address createUserAddress(RegisterRequest request) {
        Address theAddress = new Address();
        theAddress.setAddress(request.getAddress());
        theAddress.setCity(request.getTown());
        theAddress.setCountry(request.getCountry());
        theAddress.setAddressType(AddressType.HOME);
        repositoryService.save(theAddress);
        return theAddress;
    }

    private AuthUser createAuthUser(RegisterRequest request) throws Exception {
        AuthUser authUser;
        authUser = new AuthUser();
        String mobileNumber = request.getMobileNumber();
        mobileNumber = mobileNumber.replaceAll("\\D+", "");
        mobileNumber = mobileNumber.replaceAll(" ", "");
        authUser.setUserName(mobileNumber.toLowerCase());
        String newPassword = AuthUser.encodedPassword(request.getPassword());
        authUser.setPassword(newPassword);
        authUser.setMobileNumber(mobileNumber);
        repositoryService.save(authUser);
        return  authUser;
    }

    private void validateData(RegisterRequest request, StringBuilder builder) {
        builder.append(validateDateField(DATE_OF_BIRTH, request.getDateOfBirth()));
        if(StringUtils.isNotBlank(request.getEmail())) {
            if(!EmailValidator.isValidEmail(request.getEmail())) {
                builder.append("Invalid email found");
            }
        }
        builder.append(validateStringField(PASSWORD, request.getPassword()));
        builder.append(validateStringField(FIRST_NAME, request.getFirstName()));
        builder.append(validateStringField(LAST_NAME, request.getLastName()));
        builder.append(validateStringField(NATIONAL_ID, request.getNationalId()));
        builder.append(validateStringField(ADDRESS, request.getAddress()));
        builder.append(validateStringField(TOWN, request.getTown()));
        builder.append(validateStringField(COUNTRY, request.getCountry()));
        builder.append(validateStringField(MOBILE_NUMBER, request.getMobileNumber()));
        builder.append(validateStringField(ANSWER_1, request.getAnswer1()));
        builder.append(validateStringField(ANSWER_2, request.getAnswer2()));
        builder.append(validateStringField(ANSWER_3, request.getAnswer3()));
    }

    private String validateStringField(String fieldName, String fieldData
                               ){
        if(StringUtils.isBlank(fieldData)){
            return fieldName + " must not be empty";
        }
        return "";
    }
    private String validateDateField(String fieldName, LocalDate dateOfBirth
                                     ){
        if(dateOfBirth == null){
            return fieldName + " must not be empty";
        }

        int minAge = config.getIntegerProperty(Constants.REG_MIN_AGE);
        if (dateOfBirth.isAfter(LocalDate.now().minusYears(minAge))) {
            return "You must be at least " + minAge + " to register in the system";
        }
        return "";
    }
    public static final String STATUS = "status";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String NATIONAL_ID = "nationalId";
    public static final String ADDRESS = "address";
    public static final String TOWN = "town";
    public static final String COUNTRY = "country";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String ANSWER_1 = "answer1";
    public static final String ANSWER_2 = "answer2";
    public static final String ANSWER_3 = "answer3";
    private static final String[] ROLES = {"System Administrator", "Customer Service"};
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);
}
