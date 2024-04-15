package com.example.socialnetworkfx.service;


import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observable;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;
import com.example.socialnetworkfx.validators.Validator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService extends Observable {

    Repository<Long, User> userRepo;
    Repository<Tuple<Long,Long>, Friendship> friendshipRepo;
    private Validator<User> validator;
    public UserService(Repository<Long, User> userRepo, Repository<Tuple<Long,Long>,Friendship> friendshipRepo, Validator<User> validator){
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.validator = validator;
    }

    public Repository<Long, User> getUserRepo() {
        return userRepo;
    }

    public User create(String firstName, String lastName, String email, String password){
        User newUser = new User(firstName, lastName, email, this.encryptPassword(password));
        newUser.setId(findFreeID());
        validator.validate(newUser);
        userRepo.save(newUser);
        notifyObservers(ObsEvents.UserEvent);
        return newUser;
    }

    public void delete(Long id){
        userRepo.delete(id);
        Iterable<Friendship> friendships = friendshipRepo.findAll();
//        for(Friendship friendship : friendships){
//            if(friendship.isPartOf(id))
//                friendshipRepo.delete(friendship.getId());
//        }



        friendships.forEach(friendship -> {
            if(friendship.isPartOf(id))
                friendshipRepo.delete(friendship.getId());
        });
        notifyObservers(ObsEvents.UserEvent);
    }

    public User update(Long id, String firstName, String lastName, String email, String password){
        User newUser = new User(firstName, lastName, email, this.encryptPassword(password));
        newUser.setId(id);
        validator.validate(newUser);
        userRepo.update(newUser);
        notifyObservers(ObsEvents.UserEvent);
        return newUser;
    }

    public Iterable<User> getAll(){
        return userRepo.findAll();
    }

    private Long findFreeID(){
        Long id = 1L;
        while(userRepo.findOne(id).isPresent())
            id++;
        return id;
    }

    private String encryptPassword(String password){
        String encryptedpassword = null;
        try
        {

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return encryptedpassword;
    }

    public User logIn(String email, String password) {
        Iterable<User> users = userRepo.findAll();
        for(User user : users){
            if(user.getEmail().equals(email) && user.getPassword().equals(this.encryptPassword(password)))
                return user;
        }
        return null;
    }
}
