package com.example.socialnetworkfx.guiAdmin;

import com.example.socialnetworkfx.domain.FriendshipRequest;
import com.example.socialnetworkfx.domain.dto.FriendshipRequestUserDTO;
import com.example.socialnetworkfx.domain.dto.FriendshipUserDTO;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observer;
import com.example.socialnetworkfx.service.FriendshipService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class FriendshipPage implements Observer {
    @FXML
    public TableView friendshipTable;
    @FXML
    public TableColumn friendshipTableColumnUser1;
    @FXML
    public TableColumn friendshipTableColumnUser2;
    @FXML
    public TableView friendshipRequestTable;
    @FXML
    public TableColumn friendshipRequestTableColumnIDSender;
    @FXML
    public TableColumn friendshipRequestTableColumnIDReceiver;
    @FXML
    public TableColumn friendshipRequestTableColumnStatus;
    @FXML
    public Button friendshipRequestTableAcceptButton;
    @FXML
    public Button friendshipRequestTableRefuseButton;
    @FXML
    public TextField createFriendshipRequestIDSenderLabel;
    @FXML
    public TextField createFriendshipRequestIDReceiverLabel;
    @FXML
    public Button createFriendshipRequestAddButton;

    FriendshipService friendshipService;
    Stage stage;
    ObservableList<FriendshipUserDTO> friendshipsUserDtoObsList = FXCollections.observableArrayList();
    ObservableList<FriendshipRequestUserDTO> friendshipsRequestsObsList = FXCollections.observableArrayList();

    public void setUp(FriendshipService friendshipService, Stage stage) {
        this.friendshipService = friendshipService;
        friendshipService.addObserver(this);
        this.stage = stage;
        initFriendshipTable();
        initFriendshipRequestTable();

    }

    @Override
    public void update(ObsEvents message) {
        if (message.equals(ObsEvents.FriendshipEvent)) {
            friendshipsUserDtoObsList.clear();
            friendshipsUserDtoObsList.setAll(friendshipService.getAllFriendshipUserDTOs());
        }
        if (message.equals(ObsEvents.FriendshipRequestEvent)) {
            friendshipsRequestsObsList.clear();
            friendshipsRequestsObsList.setAll(friendshipService.getAllFriendshipRequestUserDTOs());
        }

    }



    public void initFriendshipTable() {
        friendshipTableColumnUser1.setCellValueFactory(new PropertyValueFactory<FriendshipUserDTO, String>("user1Name"));
        friendshipTableColumnUser2.setCellValueFactory(new PropertyValueFactory<FriendshipUserDTO, String>("user2Name"));
        friendshipTable.setItems(friendshipsUserDtoObsList);
        List<FriendshipUserDTO> friendshipUserDTOList = friendshipService.getAllFriendshipUserDTOs();
        friendshipsUserDtoObsList.setAll(friendshipUserDTOList);
    }

    public void initFriendshipRequestTable() {
        friendshipRequestTableColumnIDSender.setCellValueFactory(new PropertyValueFactory<FriendshipRequestUserDTO, String>("senderName"));
        friendshipRequestTableColumnIDReceiver.setCellValueFactory(new PropertyValueFactory<FriendshipRequestUserDTO, String>("receiverName"));
        friendshipRequestTableColumnStatus.setCellValueFactory(new PropertyValueFactory<FriendshipRequestUserDTO, String>("status"));
        friendshipRequestTable.setItems(friendshipsRequestsObsList);
        friendshipsRequestsObsList.setAll(friendshipService.getAllFriendshipRequestUserDTOs());
    }

    public void handleAddFriendshipRequestButton(ActionEvent actionEvent) {
        Long idSender = Long.parseLong(createFriendshipRequestIDSenderLabel.getText());
        Long idReceiver = Long.parseLong(createFriendshipRequestIDReceiverLabel.getText());
        friendshipService.createFriendshipRequest(idSender, idReceiver);
//        friendshipsRequestsObsList.add(newFriendshipRequest);

    }

    public void handleAcceptFriendshipRequestButton(ActionEvent actionEvent) {
        FriendshipRequestUserDTO friendshipRequestUserDTO = (FriendshipRequestUserDTO) friendshipRequestTable.getSelectionModel().getSelectedItem();
        FriendshipRequest friendshipRequest = friendshipRequestUserDTO.getFriendshipRequest();
        friendshipService.updateFriendshipRequestStatus(friendshipRequest.getIdSender(), friendshipRequest.getIdReceiver(), (long) 1);
    }

    public void handleRefuseFriendshipRequestButton(ActionEvent actionEvent) {
        FriendshipRequestUserDTO friendshipRequestUserDTO = (FriendshipRequestUserDTO) friendshipRequestTable.getSelectionModel().getSelectedItem();
        FriendshipRequest friendshipRequest = friendshipRequestUserDTO.getFriendshipRequest();
        friendshipService.updateFriendshipRequestStatus(friendshipRequest.getIdSender(), friendshipRequest.getIdReceiver(), (long) 2);
    }
}
