package com.example.socialnetworkfx.repository.fileRepository;


import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractFileRepository;

import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User> {

    public UserFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    public User extractEntity(List<String> attributes) {

        User user = new User(attributes.get(1),attributes.get(2),attributes.get(3),attributes.get(4));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName();
    }
}