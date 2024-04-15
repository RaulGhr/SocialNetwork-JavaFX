package com.example.socialnetworkfx.validators;


import com.example.socialnetworkfx.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getId() == null){
            throw new ValidationException("Invalid ID");
        } else if(entity.getFirstName().isEmpty()){
            throw new ValidationException("Invalid First Name");
        } else if (entity.getLastName().isEmpty()) {
            throw new ValidationException("Invalid Last Name");
        }
    }
}
