package com.example.socialnetworkfx.service;

import com.example.socialnetworkfx.domain.GroupChat;
import com.example.socialnetworkfx.domain.GroupChatMember;
import com.example.socialnetworkfx.domain.Message;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.domain.dto.MessageUserDTO;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observable;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;
import com.example.socialnetworkfx.validators.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupChatService extends Observable {
    Repository<Long, GroupChat> groupChatRepo;
    Repository<Tuple<Long, Long>, GroupChatMember> groupChatMemberRepo;

    Repository<Long, User> userFileRepository;

    Repository<Long, Message> messageRepo;
//    private Validator<GroupChat> validatorGroupChat;
//    private Validator<GroupChatMember> validatorGroupChatMember;

    public GroupChatService(Repository<Long, GroupChat> groupChatRepo, Repository<Tuple<Long, Long>, GroupChatMember> groupChatMemberRepo, Repository<Long, Message> messageRepo, Repository<Long, User> userFileRepository) {
        this.groupChatRepo = groupChatRepo;
        this.groupChatMemberRepo = groupChatMemberRepo;
        this.userFileRepository = userFileRepository;
//        this.validatorGroupChat = validatorGroupChat;
//        this.validatorGroupChatMember = validatorGroupChatMember;
        this.messageRepo = messageRepo;
    }

    public Repository<Long, GroupChat> getGroupChatRepo() {
        return groupChatRepo;
    }

    public Repository<Tuple<Long, Long>, GroupChatMember> getGroupChatMemberRepo() {
        return groupChatMemberRepo;
    }

    public Repository<Long, Message> getMessageRepo() {
        return messageRepo;
    }

    public GroupChat createGroupChat(String name) {
        GroupChat newGroupChat = new GroupChat(name);
        newGroupChat.setId(findFreeIDInGroupChatRepo());
//        validatorGroupChat.validate(newGroupChat);
        groupChatRepo.save(newGroupChat);
        notifyObservers(ObsEvents.GroupChatEvent);
        return newGroupChat;

    }

    public GroupChatMember addMember(Long idUser, Long idGroupChat) {
        GroupChatMember newGroupChatMember = new GroupChatMember(idUser, idGroupChat);
//        validatorGroupChatMember.validate(newGroupChatMember);
        groupChatMemberRepo.save(newGroupChatMember);
        notifyObservers(ObsEvents.GroupChatMemberEvent);
        return newGroupChatMember;
    }

    public Message sendMessage(Long idGroupChat, Long idUser, String message, Long replyMessageId) {
        Iterable<GroupChatMember> groupChatsMembers = groupChatMemberRepo.findAll();
        boolean isMember = false;
        for (GroupChatMember groupChatMember : groupChatsMembers) {
            if (Objects.equals(groupChatMember.getIdGroup(), idGroupChat) && Objects.equals(groupChatMember.getIdUser(), idUser)) {
                isMember = true;
                break;
            }
        }
        if (!isMember)
            throw new RuntimeException("User is not a member of this group chat!");


        Message newMessage = new Message(idGroupChat, idUser, message, replyMessageId);
        newMessage.setId(findFreeIDInMessageRepo());
        messageRepo.save(newMessage);
        notifyObservers(ObsEvents.MessageEvent);
        return newMessage;
    }

    public List<Message> getMessegesFromGroupChat(Long idGroupChat) {

        List<Message> messenges = new ArrayList<>();
        for (Message message : messageRepo.findAll()) {
            if (message.getIdGroupChat() == idGroupChat)
                messenges.add(message);
        }
        messenges.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return messenges;
    }

    public Iterable<GroupChat> getAllGroupChats() {
        return groupChatRepo.findAll();
    }

    public List<GroupChat> getAllGroupChatsOfMember(Long userId){
        Iterable<GroupChatMember> groupChatMembers = groupChatMemberRepo.findAll();
        List<GroupChat> groupChats = new ArrayList<>();
        for (GroupChatMember groupChatMember : groupChatMembers) {
            if (groupChatMember.getIdUser().equals(userId)){
                groupChats.add(groupChatRepo.findOne(groupChatMember.getIdGroup()).get());
            }
        }
        return groupChats;

    }

    public Iterable<GroupChatMember> getAllGroupChatsMembers() {
        return groupChatMemberRepo.findAll();
    }

    public List<MessageUserDTO> getAllMessageUserDTOFromGroupChat(Long idGroupChat) {
        Iterable<Message> messages = messageRepo.findAll();
        List<MessageUserDTO> messageUserDTOList = new ArrayList<>();
        messages.forEach(message -> {
            if (message.getIdGroupChat().equals(idGroupChat)) {
                User user = userFileRepository.findOne(message.getIdUser()).get();
                MessageUserDTO messageUserDTO = new MessageUserDTO(message, user);
                messageUserDTOList.add(messageUserDTO);
            }
        });

        return messageUserDTOList;
    }

    private Long findFreeIDInGroupChatRepo() {
        Long id = 1L;
        while (groupChatRepo.findOne(id).isPresent())
            id++;
        return id;
    }

    private Long findFreeIDInMessageRepo() {
        Long id = 1L;
        while (messageRepo.findOne(id).isPresent())
            id++;
        return id;
    }

}
