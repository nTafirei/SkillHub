package com.marotech.recording.ws;

import com.marotech.recording.api.HttpCode;
import com.marotech.recording.api.ResponseType;
import com.marotech.recording.api.ServiceResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reg-data")
public class RegDataController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Fetches security questions and vendors from the system",
            notes = "Fetches security questions and vendors from the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registration data is included"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ServiceResponse.class)
    })
    public ResponseEntity<ServiceResponse>
    fetchRegData() {
        ServiceResponse response = new ServiceResponse();
        response.setResponseType(ResponseType.REG_DATA);
        try {
            String securityQuestion1 = config.getProperty(SEC_QUESTION_1);
            String securityQuestion2 = config.getProperty(SEC_QUESTION_2);
            String securityQuestion3 = config.getProperty(SEC_QUESTION_3);
            response.getAdditionalInfo().put(SEC_QUESTION_1, securityQuestion1);
            response.getAdditionalInfo().put(SEC_QUESTION_2, securityQuestion2);
            response.getAdditionalInfo().put(SEC_QUESTION_3, securityQuestion3);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.setCode(HttpCode.INTERNAL_SERVER_ERROR);
            response.setMessage("An error occurred in the system. Please contact our customer service team");
            LOG.error("Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    private static final Logger LOG = LoggerFactory.getLogger(RegDataController.class);
}
