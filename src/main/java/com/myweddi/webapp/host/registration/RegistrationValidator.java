package com.myweddi.webapp.host.registration;

import com.myweddi.user.RegistrationForm;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegistrationValidator implements Validator {

    private UserAuthRepository userAuthRepository;

    public RegistrationValidator(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegistrationForm rf = (RegistrationForm) o;

        if(!rf.getPassword().equals(rf.getPassword2()))
            errors.rejectValue("password", "valid.msg.passwordnotsame");

        if(userAuthRepository.existsByUsername(rf.getUsername()))
            errors.rejectValue("username", "valid.msg.usernameexists");

    }
}
