package com.example.socialnetworkfx.ui;


import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.service.FriendshipService;
import com.example.socialnetworkfx.service.UserService;
import com.example.socialnetworkfx.service.NetworkService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class UI {
    UserService userService;
    FriendshipService friendshipService;
    NetworkService networkService;
    private Map<String, Consumer<String[]>> menuOptions = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public UI(UserService userService, FriendshipService friendshipService, NetworkService networkService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkService = networkService;

        initComands();
    }

    private void initComands() {
        menuOptions.put("addUser", this::addUser);
        menuOptions.put("delUser", this::deleteUser);
        menuOptions.put("printAllUsers", this::printAllUsers);

        menuOptions.put("addFr", this::addFriendship);
        menuOptions.put("delFr", this::deleteFriendship);
        menuOptions.put("printAllFrs", this::printAllFriendship);

        menuOptions.put("printAllCom", this::printAllCommunities);
        menuOptions.put("printNrOfCom", this::printNumberOfCommunities);
        menuOptions.put("printBiggestCom", this::printBiggestCommunity);

        menuOptions.put("userFrs", this::userFriendshipsByMonth);

    }

    public void run() {
        System.out.println("Welcome!");
        while (true) {
            try {
                System.out.print(">>>");
                String option = scanner.nextLine();
                String[] arguments = option.split(" ");
                String command = arguments[0];
                if (command.equals("exit"))
                    break;
                if (menuOptions.containsKey(command)) {
                    menuOptions.get(command).accept(arguments);
                } else {
                    System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    ///USER COMMANDS
    private void addUser(String[] arguments) {
        if (arguments.length != 3)
            throw new RuntimeException("Invalid number of arguments!");
//        userService.create(arguments[1], arguments[2]);
        System.out.println("User added successfully!");
    }

    private void deleteUser(String[] arguments) {
        if (arguments.length != 2)
            throw new RuntimeException("Invalid number of arguments!");
        userService.delete(Long.parseLong(arguments[1]));
        System.out.println("User deleted successfully!");
    }

    private void printAllUsers(String[] arguments) {
        for (User user : userService.getAll())
            System.out.println(user);
    }

    ///FRIENDSHIP COMMANDS
    private void addFriendship(String[] arguments) {
        if (arguments.length != 3)
            throw new RuntimeException("Invalid number of arguments!");
        friendshipService.createFriendship(Long.parseLong(arguments[1]), Long.parseLong(arguments[2]));
        System.out.println("Friendship added successfully!");
    }

    private void deleteFriendship(String[] arguments) {
        if (arguments.length != 3)
            throw new RuntimeException("Invalid number of arguments!");
        friendshipService.deleteFriendship(Long.parseLong(arguments[1]), Long.parseLong(arguments[2]));
        System.out.println("Friendship deleted successfully!");
    }

    private void printAllFriendship(String[] arguments) {
        for (Friendship friendship : friendshipService.getAllFriendships())
            System.out.println(friendship);
    }

    ///NETWORK COMMANDS

    private void printAllCommunities(String[] arguments) {
        for (Iterable<Long> community : networkService.allNetwork())
            System.out.println(community);
    }

    private void printNumberOfCommunities(String[] arguments) {
        System.out.println(networkService.numberOfCommunities());
    }

    private void printBiggestCommunity(String[] arguments) {
        System.out.println(networkService.biggestCommunity());
    }

    private void userFriendshipsByMonth(String[] arguments) {
        if (arguments.length != 3)
            throw new RuntimeException("Invalid number of arguments!");

        List<String> lista = friendshipService.getUserFriendships(Long.parseLong(arguments[1]), Integer.parseInt(arguments[2]));
        if(lista.isEmpty())
            System.out.println("No friendships found!");
        else
            lista.forEach(System.out::println);
    }


}
