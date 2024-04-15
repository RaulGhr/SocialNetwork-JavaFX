package com.example.socialnetworkfx.observer;

import java.util.ArrayList;

public class Observable {
    ArrayList<Observer> observers = new ArrayList<>();
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    public void notifyObservers(ObsEvents message){
        observers.forEach(observer -> observer.update(message));
    }

    public void removeObserver(Observer observer){
        observers.remove(observer);
    }


}
