package com.example.socialnetworkfx.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}