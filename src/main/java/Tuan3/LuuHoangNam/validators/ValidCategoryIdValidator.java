package Tuan3.LuuHoangNam.validators;

import Tuan3.LuuHoangNam.entities.Category;
import Tuan3.LuuHoangNam.validators.annotations.ValidCategoryId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class ValidCategoryIdValidator implements
        ConstraintValidator<ValidCategoryId, Category> {
    @Override
    public boolean isValid(Category category,
                           ConstraintValidatorContext context) {
        return category != null && category.getId() != null;
    }
}

