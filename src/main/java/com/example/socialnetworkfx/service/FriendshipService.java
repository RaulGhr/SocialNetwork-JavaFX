package com.example.socialnetworkfx.service;



import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.FriendshipRequest;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.domain.dto.FriendshipRequestUserDTO;
import com.example.socialnetworkfx.domain.dto.FriendshipUserDTO;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observable;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;
import com.example.socialnetworkfx.validators.ValidationException;
import com.example.socialnetworkfx.validators.Validator;


import java.util.ArrayList;
import java.util.List;

public class FriendshipService extends Observable {
    Repository<Tuple<Long,Long>, Friendship> friendshipRepository;
    Repository<Tuple<Long,Long>, FriendshipRequest> friendshipRequestRepository;
    Repository<Long, User> userFileRepository;

    private Validator<Friendship> validator;

    public FriendshipService(Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository, Repository<Long, User> userFileRepository, Repository<Tuple<Long,Long>, FriendshipRequest> friendshipRequestRepository, Validator<Friendship> validator){
        this.friendshipRepository = friendshipFileRepository;
        this.userFileRepository = userFileRepository;
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.validator = validator;
    }

    public void createFriendship(Long id1, Long id2){

        if (userFileRepository.findOne(id1).isEmpty() || userFileRepository.findOne(id2).isEmpty())
            throw new ValidationException("One or both of the users don't exist!");

        Friendship friendship = new Friendship(id1,id2);
        validator.validate(friendship);
        friendshipRepository.save(friendship);


        super.notifyObservers(ObsEvents.FriendshipEvent);

    }

    public FriendshipRequest createFriendshipRequest(Long idSender, Long idReceiver){

        if (userFileRepository.findOne(idSender).isEmpty() || userFileRepository.findOne(idReceiver).isEmpty())
            throw new ValidationException("One or both of the users don't exist!");

        FriendshipRequest friendshipRequest = new FriendshipRequest(idSender,idReceiver, (long) 0);
        friendshipRequestRepository.save(friendshipRequest);
        super.notifyObservers(ObsEvents.FriendshipRequestEvent);

        return friendshipRequest;
    }

    public FriendshipRequest updateFriendshipRequestStatus(Long idSender, Long idReceiver, Long status){
        FriendshipRequest newFriendshipRequest = new FriendshipRequest(idSender,idReceiver,status);
        Tuple<Long,Long> idToUpdate = newFriendshipRequest.getId();
        friendshipRequestRepository.findOne(idToUpdate).ifPresentOrElse(friendshipRequest -> {
            if(status == 1) {
                createFriendship(idSender, idReceiver);
            }
            friendshipRequestRepository.update(newFriendshipRequest);
        }, () -> {
            throw new ValidationException("The friendship request doesn't exist!");
        });
        super.notifyObservers(ObsEvents.FriendshipRequestEvent);
        return newFriendshipRequest;
    }

    public void deleteFriendship(Long id1, Long id2){
        Tuple<Long,Long> idToDelete = new Tuple<>(id1,id2);

        friendshipRepository.findOne(idToDelete).ifPresentOrElse(friendship -> {
            friendshipRepository.delete(idToDelete);
        }, () -> {
            throw new ValidationException("The friendship doesn't exist!");
        });
        super.notifyObservers(ObsEvents.FriendshipEvent);

    }

    public Iterable<Friendship> getAllFriendships(){
        return friendshipRepository.findAll();
    }

    public Iterable<FriendshipRequest> getAllFriendshipRequests(){
        return friendshipRequestRepository.findAll();
    }

    public List<FriendshipUserDTO> getAllFriendshipUserDTOs(){
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<FriendshipUserDTO> friendshipUserDTOList = new ArrayList<>();
        friendships.forEach(friendship -> {
            User user1 = userFileRepository.findOne(friendship.getId().getLeft()).get();
            User user2 = userFileRepository.findOne(friendship.getId().getRight()).get();
            FriendshipUserDTO friendshipUserDTO = new FriendshipUserDTO(friendship,user1,user2);
            friendshipUserDTOList.add(friendshipUserDTO);
        });
        return friendshipUserDTOList;
    }

    public List<FriendshipRequestUserDTO> getAllFriendshipRequestUserDTOs(){
        Iterable<FriendshipRequest> friendshipRequests = friendshipRequestRepository.findAll();
        List<FriendshipRequestUserDTO> friendshipRequestUserDTOList = new ArrayList<>();
        friendshipRequests.forEach(friendshipRequest -> {
            User user1 = userFileRepository.findOne(friendshipRequest.getIdSender()).get();
            User user2 = userFileRepository.findOne(friendshipRequest.getIdReceiver()).get();
            FriendshipRequestUserDTO friendshipRequestUserDTO = new FriendshipRequestUserDTO(friendshipRequest,user1,user2);
            friendshipRequestUserDTOList.add(friendshipRequestUserDTO);
        });
        return friendshipRequestUserDTOList;
    }

    public List<String> getUserFriendships(Long id, int month){
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<String> userFriends = new ArrayList<>();
        friendships.forEach(friendship -> {
            if(friendship.isPartOf(id) && friendship.getDate().getMonthValue() == month){
                if(friendship.getId().getLeft() == id) {
                    User user = userFileRepository.findOne(friendship.getId().getRight()).get();
                    userFriends.add(user.getFirstName() + " " + user.getLastName() + " " + friendship.getDate());
                }
                else {
                    User user = userFileRepository.findOne(friendship.getId().getLeft()).get();
                    userFriends.add(user.getFirstName() + " " + user.getLastName() + " " + friendship.getDate());
                }

            }
        });

        return userFriends;

    }

}
