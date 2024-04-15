package com.example.socialnetworkfx.guiAdmin;

import com.example.socialnetworkfx.domain.GroupChat;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.domain.dto.MessageUserDTO;
import com.example.socialnetworkfx.guiAdmin.alerts.ActionAlert;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observer;
import com.example.socialnetworkfx.service.GroupChatService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

public class GroupChatUserView implements Observer {
    public TableView groupTable;
    public TableColumn tableGroupColumnName;
    public TableView tableMessage;
    public TableColumn tableMessageColumnId;
    public TableColumn tableMessageColumnUser;
    public TableColumn tableMessageColumnDate;
    public TableColumn tableMessageColumnReplyTo;
    public TableColumn tableMessageColumnMessage;
    public TextField lableSendMessge;
    public Button buttonSendMessage;
    public CheckBox checkboxReply;

    GroupChatService groupChatService;
    Stage stage;
    Long idGroupChat = null;
    User user;
    ObservableList<GroupChat> groupChatsObsList = FXCollections.observableArrayList();
    ObservableList<MessageUserDTO> messagesObsList = FXCollections.observableArrayList();

    public void setUp(User user, GroupChatService groupChatService, Stage stage){
        this.groupChatService = groupChatService;
        groupChatService.addObserver(this);
        this.user = user;

        this.stage = stage;
        initGroupTable();
        initMessageTable();
    }

    @Override
    public void update(ObsEvents message) {
        if (message == ObsEvents.GroupChatEvent){
            Iterable<GroupChat> allGroupChats = groupChatService.getAllGroupChatsOfMember(this.user.getId());
            List<GroupChat> allGroupChatsList = StreamSupport.stream(allGroupChats.spliterator(), false).toList();
            groupChatsObsList.clear();
            groupChatsObsList.setAll(allGroupChatsList);
        }
        if (message == ObsEvents.MessageEvent){
            if (idGroupChat != null){
                messagesObsList.clear();
                messagesObsList.setAll(groupChatService.getAllMessageUserDTOFromGroupChat(this.idGroupChat));
            }
        }


    }

    void initGroupTable(){
        tableGroupColumnName.setCellValueFactory(new PropertyValueFactory<GroupChat, String>("name"));
        groupTable.setItems(groupChatsObsList);
        this.update(ObsEvents.GroupChatEvent);

        groupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GroupChat>() {
            @Override
            public void changed(ObservableValue<? extends GroupChat> observable, GroupChat oldValue, GroupChat newValue) {
                idGroupChat = newValue.getId();
                update(ObsEvents.MessageEvent);
            }
        });
    }

    void initMessageTable(){
        tableMessageColumnId.setCellValueFactory(new PropertyValueFactory<MessageUserDTO,Long>("messageId"));
        tableMessageColumnReplyTo.setCellValueFactory(new PropertyValueFactory<MessageUserDTO,Long>("messageReplyId"));
        tableMessageColumnUser.setCellValueFactory(new PropertyValueFactory<MessageUserDTO,String>("userName"));
        tableMessageColumnDate.setCellValueFactory(new PropertyValueFactory<MessageUserDTO, Date>("messageDate"));
        tableMessageColumnMessage.setCellValueFactory(new PropertyValueFactory<MessageUserDTO,String>("messageText"));
        tableMessage.setItems(messagesObsList);
        this.update(ObsEvents.MessageEvent);
    }

    public void sendMessageHandler(){
        String message = lableSendMessge.getText();
        if (message.equals("")){
            ActionAlert.showErrorMessage(null, "Message can't be empty!");
            return;
        }

        Long replyTo = null;
        if (checkboxReply.isSelected()){
            MessageUserDTO messageUserDTO = (MessageUserDTO) tableMessage.getSelectionModel().getSelectedItem();
            if (messageUserDTO == null){
                ActionAlert.showErrorMessage(null, "Select a message to reply to!");
                return;
            }
            replyTo = messageUserDTO.getMessageId();
        }
        groupChatService.sendMessage(idGroupChat, user.getId(), message, replyTo);
        lableSendMessge.setText("");
        checkboxReply.setSelected(false);
    }
}
