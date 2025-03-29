package Tuan3.LuuHoangNam.validators;

import Tuan3.LuuHoangNam.services.UserService;
import Tuan3.LuuHoangNam.validators.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(force = true)
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(userService == null)
        {
            return true;
        }
        return userService.findByUsername(username).isEmpty();
    }
}