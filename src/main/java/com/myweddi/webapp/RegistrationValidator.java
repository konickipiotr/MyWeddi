package com.myweddi.webapp;

import com.myweddi.user.RegistrationForm;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegistrationValidator implements Validator {

    private final UserAuthRepository userAuthRepository;

    public RegistrationValidator(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return RegistrationForm.class.equals(aClass);
    }

    @Override
    public void validate(@NotNull Object o, @NotNull Errors errors) {
        RegistrationForm rf = (RegistrationForm) o;

        if(!rf.getPassword().equals(rf.getPassword2()))
            errors.rejectValue("password", "valid.msg.passwordnotsame");

        if(userAuthRepository.existsByUsername(rf.getUsername()))
            errors.rejectValue("username", "valid.msg.usernameexists");
    }
}
