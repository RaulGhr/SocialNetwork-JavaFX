package com.example.socialnetworkfx.guiAdmin;

import com.example.socialnetworkfx.domain.GroupChat;
import com.example.socialnetworkfx.domain.GroupChatMember;
import com.example.socialnetworkfx.domain.Message;
import com.example.socialnetworkfx.guiAdmin.alerts.ActionAlert;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observer;
import com.example.socialnetworkfx.repository.paging.PageableDBRepository;
import com.example.socialnetworkfx.service.GroupChatService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.StreamSupport;

public class GroupChatPage implements Observer {
    @FXML
    public TableView groupTable;
    @FXML
    public TableColumn groupTableColumnID;
    @FXML
    public TableColumn groupTableColumnName;
    @FXML
    public TableView groupMembersTable;
    @FXML
    public TableColumn groupMembersTableColumnIDUser;
    @FXML
    public TableColumn groupMembersTableColumnIDGroup;
    @FXML
    public TextField createGroupNameLabel;
    @FXML
    public Button createGroupCreateButton;
    @FXML
    public TextField addUserGroupIDLabel;
    @FXML
    public TextField addUserUserIDLabel;
    @FXML
    public Button addUserAddButton;
    @FXML
    public TextField sendMessageGroupIDLabel;
    @FXML
    public TextField sendMessageUserIDLabel;
    @FXML
    public TextField sendMessageTextLabel;
    @FXML
    public TextField sendMessageReplayLabel;
    @FXML
    public Button sendMessageSendButton;
    @FXML
    public TableView messageTable;
    @FXML
    public TableColumn messageTableColumnID;
    @FXML
    public TableColumn messageTableColumnUser;
    @FXML
    public TableColumn messageTableColumnDate;
    @FXML
    public TableColumn messageTableColumnReplay;
    @FXML
    public TableColumn messageTableColumnText;
    public Button buttonNextGroup;
    public Button buttonPrevGroup;
    public ComboBox comboBoxGroup;
    public Button buttonNextGroupMember;
    public Button buttonPrevGroupMember;
    public ComboBox comboBoxGroupMember;
//    public Button buttonPrevMessage;
//    public Button buttonNextMessage;
//    public ComboBox comboBoxMessage;

    GroupChatService groupChatService;
    Stage stage;
    Long idGroupChat = null;

    PageableDBRepository pageableDBRepositoryGroupChat;

    PageableDBRepository pageableDBRepositoryGroupChatMember;

    PageableDBRepository pageableDBRepositoryMessage;

    ObservableList<GroupChat> groupChatsObsList = FXCollections.observableArrayList();
    ObservableList<GroupChatMember> groupChatMembersObsList = FXCollections.observableArrayList();
    ObservableList<Message> messagesObsList = FXCollections.observableArrayList();

    public void setUp(GroupChatService groupChatService, Stage stage) {
        this.groupChatService = groupChatService;
        groupChatService.addObserver(this);
        this.stage = stage;
        this.pageableDBRepositoryGroupChat = new PageableDBRepository<>(groupChatService.getGroupChatRepo(), buttonPrevGroup, buttonNextGroup, comboBoxGroup, ObsEvents.GroupChatEvent);
        this.pageableDBRepositoryGroupChat.addObserver(this);
        this.pageableDBRepositoryGroupChatMember = new PageableDBRepository<>(groupChatService.getGroupChatMemberRepo(), buttonPrevGroupMember, buttonNextGroupMember, comboBoxGroupMember, ObsEvents.GroupChatMemberEvent);
        this.pageableDBRepositoryGroupChatMember.addObserver(this);
//        this.pageableDBRepositoryMessage = new PageableDBRepository<>(groupChatService.getMessageRepo(), buttonPrevMessage, buttonNextMessage, comboBoxMessage, ObsEvents.MessageEvent);
        this.initializeTableGroupChats();
        this.initializeTableGroupChatMembers();
        this.initializeTableMessages();
    }

    @Override
    public void update(ObsEvents message) {
        if (message == ObsEvents.GroupChatEvent) {
            groupChatsObsList.clear();
            groupChatsObsList.setAll(pageableDBRepositoryGroupChat.findAll());
        }
        if (message == ObsEvents.GroupChatMemberEvent) {
            groupChatMembersObsList.clear();
            groupChatMembersObsList.setAll(pageableDBRepositoryGroupChatMember.findAll());
        }
        if (message == ObsEvents.MessageEvent) {
            List<Message> messages = groupChatService.getMessegesFromGroupChat(this.idGroupChat);
            List<Message> messagesList = messages.subList(Math.max(messages.size() - 5, 0), messages.size());
            messagesObsList.clear();
            messagesObsList.setAll(messagesList);
        }
    }

    public void initializeTableGroupChats() {
        groupTableColumnID.setCellValueFactory(new PropertyValueFactory<GroupChat, Long>("id"));
        groupTableColumnName.setCellValueFactory(new PropertyValueFactory<GroupChat, String>("name"));
        groupTable.setItems(groupChatsObsList);

        List<GroupChat> allGroupChatsList = pageableDBRepositoryGroupChat.findAll();
        groupChatsObsList.setAll(allGroupChatsList);

        groupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GroupChat>() {
             @Override
             public void changed(ObservableValue<? extends GroupChat> observable, GroupChat oldValue, GroupChat newValue) {
                    List<Message> messages = groupChatService.getMessegesFromGroupChat(newValue.getId());
                    List<Message> messagesList = messages.subList(Math.max(messages.size() - 5, 0), messages.size());
                    messagesObsList.setAll(messagesList);
                    idGroupChat = newValue.getId();
             }
        });
    }

    public void initializeTableGroupChatMembers() {
        groupMembersTableColumnIDUser.setCellValueFactory(new PropertyValueFactory<GroupChatMember, Long>("idUser"));
        groupMembersTableColumnIDGroup.setCellValueFactory(new PropertyValueFactory<GroupChatMember, Long>("idGroup"));
        groupMembersTable.setItems(groupChatMembersObsList);

        List<GroupChatMember> allGroupChatMembersList = pageableDBRepositoryGroupChatMember.findAll();
        groupChatMembersObsList.setAll(allGroupChatMembersList);
    }

    public void initializeTableMessages() {
        messageTableColumnID.setCellValueFactory(new PropertyValueFactory<Message, Long>("id"));
        messageTableColumnUser.setCellValueFactory(new PropertyValueFactory<Message, Long>("idUser"));
        messageTableColumnDate.setCellValueFactory(new PropertyValueFactory<Message, String>("date"));
        messageTableColumnReplay.setCellValueFactory(new PropertyValueFactory<Message, Long>("replyTo"));
        messageTableColumnText.setCellValueFactory(new PropertyValueFactory<Message, String>("text"));
        messageTable.setItems(messagesObsList);
    }

    public void createGroupChatHandler() {
        String name = createGroupNameLabel.getText();
        if (name.equals("")) {

        } else {
            GroupChat newGroupChat = groupChatService.createGroupChat(name);
//            groupChatsObsList.add(newGroupChat);
        }
    }

    public void addUserToGroupChatHandler() {
        Long idGroupChat = Long.parseLong(addUserGroupIDLabel.getText());
        Long idUser = Long.parseLong(addUserUserIDLabel.getText());
        GroupChatMember newGroupChatMember = groupChatService.addMember(idUser, idGroupChat);
//        groupChatMembersObsList.add(newGroupChatMember);
    }

    public void sendMessageHandler() {
        try {
//            Long idGroupChat = Long.parseLong(sendMessageGroupIDLabel.getText());
            Long idGroupChat = this.idGroupChat;
            Long idUser = Long.parseLong(sendMessageUserIDLabel.getText());
            String message = sendMessageTextLabel.getText();
            Long replyMessageId = null;
            if (sendMessageReplayLabel.getText() != "") {
                replyMessageId = Long.parseLong(sendMessageReplayLabel.getText());
            }
            groupChatService.sendMessage(idGroupChat, idUser, message, replyMessageId);
//            if (idGroupChat.equals(this.idGroupChat)) {
//                messagesObsList.add(newMessage);
//            }
        } catch (Exception e) {
            ActionAlert.showErrorMessage(stage, e.getMessage());
        }
    }


}
