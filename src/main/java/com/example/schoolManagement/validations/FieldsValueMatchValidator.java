package com.example.schoolManagement.validations;

import com.example.schoolManagement.annotation.FieldsValueMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        // the value object will recieve entire object of the model for ex:- Person class
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
        if(fieldValue != null){
            return fieldValue.equals(fieldMatchValue);
        }else{
            return fieldMatchValue == null;
        }


//        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
//        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
//        if(fieldValue != null){
//           if(fieldValue.toString().startsWith("$2a")){
//               return true;
//           }else{
//               return fieldValue.equals(fieldMatchValue);
//           }
//        }else{
//            return fieldMatchValue == null;
//        }
    }
}
