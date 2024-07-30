package bg.softuni.footscore.validation.validators;

import bg.softuni.footscore.model.dto.RegisterUserDto;
import bg.softuni.footscore.validation.annotations.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegisterUserDto> {

    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(RegisterUserDto registerUserDto, ConstraintValidatorContext context) {

        final String password = registerUserDto.getPassword();
        final String confirmPassword = registerUserDto.getConfirmPassword();

        if (password == null && confirmPassword == null) {

            return true;
        } else {

            boolean passwordMatch = password != null && password.equals(confirmPassword);

            if (!passwordMatch) {

                HibernateConstraintValidatorContext hibernateContext =
                        context.unwrap(HibernateConstraintValidatorContext.class);

                hibernateContext
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }

            return passwordMatch;
        }
    }
}