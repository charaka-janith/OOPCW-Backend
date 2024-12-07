package iit.y3.oopcw.control.impl;

import iit.y3.oopcw.common.Constants;
import iit.y3.oopcw.control.User_CTRL;
import iit.y3.oopcw.dto.User_DTO;
import iit.y3.oopcw.dto.request.CustomerInfo_RQST;
import iit.y3.oopcw.dto.request.ForgetPassword_RQST;
import iit.y3.oopcw.dto.request.PasswordUpdate_RQST;
import iit.y3.oopcw.dto.response.Api_RSPNS;
import iit.y3.oopcw.dto.response.CommonMessage_RSPNS;
import iit.y3.oopcw.dto.response.PasswordUpdate_RSPNS;
import iit.y3.oopcw.dto.response.User_RSPNS;
import iit.y3.oopcw.exceptions.PreConditionFailedException;
import iit.y3.oopcw.model.User;
import iit.y3.oopcw.service.User_SRVC;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Component
public class User_CTRL_IMPL implements User_CTRL {

    private static final Logger logger = LoggerFactory.getLogger("exceptions");
    private final User_SRVC userService;
    private final ModelMapper modelMapper;

    @Autowired
    public User_CTRL_IMPL(final User_SRVC userService
            , final ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<Api_RSPNS> registerCustomer(@Valid CustomerInfo_RQST customerInfoRequest) {
        logger.info("Calling Register Customer API. Customer Info: {}", customerInfoRequest);
        this.userService.registerCustomer(this.modelMapper.map(customerInfoRequest.getCusInfo(), User.class));
        return new Api_RSPNS.ApiResponseBuilder<>()
                .withApiStatusCode(Constants.Status.SUCCESS)
                .withMessage(Constants.Status.SUCCESS.description())
                .withStatusCode(HttpStatus.OK.value())
                .withData("User Saved Successfully!").build();
    }

    @Override
    public ResponseEntity<Api_RSPNS> registerVendor(@Valid CustomerInfo_RQST customerInfoRequest) {
        logger.info("Calling Register Vendor API. Vendor Info: {}", customerInfoRequest);
        this.userService.registerVendor(this.modelMapper.map(customerInfoRequest.getCusInfo(), User.class));
        return new Api_RSPNS.ApiResponseBuilder<>()
                .withApiStatusCode(Constants.Status.SUCCESS)
                .withMessage(Constants.Status.SUCCESS.description())
                .withStatusCode(HttpStatus.OK.value())
                .withData("Vendor Saved Successfully!").build();
    }

    @Override
    public ResponseEntity<Api_RSPNS> validateUserName(String userName) {
        return this.userService.validateUserName(userName);
    }

    @Override
    public ResponseEntity<Api_RSPNS> resetPassword(@Valid PasswordUpdate_RQST passwordUpdateRequest) {
        PasswordUpdate_RSPNS passwordUpdateResponse = PasswordUpdate_RSPNS.builder().passwordMatch(
                this.userService.resetPassword( // TODO: validate userId against current user since he should not be able to reset other users' password
                        passwordUpdateRequest.getId(), // TODO : change id-->userId for better readability
                        passwordUpdateRequest.getNewPassword(),
                        passwordUpdateRequest.getOldPassword()
                )).build();
        if (passwordUpdateResponse.getPasswordMatch()) {
            return new Api_RSPNS.ApiResponseBuilder<>().withApiStatusCode(Constants.Status.SUCCESS)
                    .withMessage(Constants.Status.SUCCESS.description()).withStatusCode(HttpStatus.OK.value())
                    .withData("Password Changed Successfully").build();
        } else {
            return new Api_RSPNS.ApiResponseBuilder<>().withApiStatusCode(Constants.Status.FAILURE)
                    .withMessage(Constants.Status.FAILURE.description()).withStatusCode(HttpStatus.OK.value())
                    .withData("Error while changing Password").build();
        }
    }

    @Override
    public ResponseEntity<Api_RSPNS> checkPasswordExpiration() {
        User user = this.userService.getCurrentUser();
        long numOfDaysBetween = ChronoUnit.DAYS.between(LocalDate.now(), user.getExpiryDate());
        if (numOfDaysBetween < 10) {
            CommonMessage_RSPNS commonMessageResponse = CommonMessage_RSPNS.builder().message("Your Password will be expired in " + numOfDaysBetween + " days.").build();
            return new Api_RSPNS.ApiResponseBuilder<>().withApiStatusCode(Constants.Status.SUCCESS)
                    .withMessage(Constants.Status.SUCCESS.description()).withStatusCode(HttpStatus.OK.value())
                    .withData(commonMessageResponse).build();
        }
        throw new PreConditionFailedException("Your Password will be expired in " + numOfDaysBetween + " days.");
    }

    @Override
    public User_RSPNS findCurrent() {
        return User_RSPNS.builder().users(
                Collections.singletonList(
                        this.modelMapper.map(this.userService.getCurrentUser()
                                , User_DTO.class))).build();
    }

    @Override
    public ResponseEntity<Api_RSPNS> forgetPassword(
            @Valid @RequestBody ForgetPassword_RQST request) {
        userService.forgetPassword(request.getUserName());
        return new Api_RSPNS.ApiResponseBuilder<>()
                .withApiStatusCode(Constants.Status.SUCCESS)
                .withMessage(Constants.Status.SUCCESS.description())
                .withStatusCode(HttpStatus.OK.value())
                .withData("Password reset mail sent successfully").build();

        /*if (request.getPassword().equalsIgnoreCase(request.getRetypePassword())) {
        } else {
            logger.error("New password and Retyped password does not match");
            return new ApiResponse
                    .ApiResponseBuilder<>()
                    .withApiStatusCode(Status.FAILURE)
                    .withMessage("New password and Retyped password does not match")
                    .withStatusCode(HttpStatus.OK.value())
                    .build();
        }*/
    }
}