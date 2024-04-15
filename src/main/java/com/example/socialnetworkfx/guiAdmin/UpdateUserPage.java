package com.example.socialnetworkfx.guiAdmin;

import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.guiAdmin.alerts.ActionAlert;
import com.example.socialnetworkfx.service.UserService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateUserPage {
    public TextField emailField;
    public TextField passwordField;
    UserService userService;
    Stage stage;
    ObservableList<User> usersObsList;
    User user;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;


    public void setUp(UserService userService, Stage stage,ObservableList<User> usersObsList,User user){
        this.userService=userService;
        this.stage=stage;
        this.usersObsList=usersObsList;
        this.user=user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());


    }

    public void updateUser(){
        String firstName=firstNameField.getText();
        String lastName=lastNameField.getText();
        String email=emailField.getText();
        String password=passwordField.getText();
        if(firstName.equals("") || lastName.equals("") || emailField.getText().equals("") || passwordField.getText().equals("")){
            ActionAlert.showErrorMessage(stage,"You must complete all fields!");
        }
        else{
            userService.update(user.getId(),firstName,lastName,email,password);
            stage.close();
        }
    }
}
