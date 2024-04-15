package com.example.socialnetworkfx.validators;


import com.example.socialnetworkfx.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getId() == null){
            throw new ValidationException("Invalid ID");
        } else if(entity.getId().getLeft() == null){
            throw new ValidationException("Invalid Left ID");
        } else if(entity.getId().getRight() == null){
            throw new ValidationException("Invalid Right ID");
        }

    }
}
