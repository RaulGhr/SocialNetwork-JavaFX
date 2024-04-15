package com.example.socialnetworkfx.repository.paging;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observable;
import com.example.socialnetworkfx.repository.abstractRepository.AbstractDBRepository;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.List;

public class PageableDBRepository<ID, E extends Entity<ID>> extends Observable {

    Repository<ID,E> abstractDBRepository;
    Integer pageNumber;
    Integer pageSize;
    Integer totalNumberOfElements;
    ObsEvents obsEvents;
    public Button buttonPrevPage;
    public Button buttonNextPage;
    public ComboBox comboBoxElems;

    public PageableDBRepository(Repository<ID,E> abstractDBRepository,
                                Button previousButton, Button nextButton, ComboBox comboBoxElems, ObsEvents obsEvents){
        this.abstractDBRepository = abstractDBRepository;
        this.pageNumber = 0;
        this.pageSize = 5;
        this.buttonNextPage = nextButton;
        this.buttonPrevPage = previousButton;
        this.comboBoxElems = comboBoxElems;
        this.totalNumberOfElements = abstractDBRepository.size();
        this.obsEvents = obsEvents;
        this.guiInit();
        handlePagingNavigationChecks();


    }

    public List<E> findAll() {
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Page<E> page = this.abstractDBRepository.findAll(pageable);
        totalNumberOfElements = page.getTotalNumberOfElements();
        return page.getElementsOnPage();
    }

    public List<E> goToPreviousPage(){
        pageNumber--;
        handlePagingNavigationChecks();
        super.notifyObservers(obsEvents);
        return findAll();
    }

    public List<E> goToNextPage(){
        pageNumber++;
        handlePagingNavigationChecks();
        super.notifyObservers(obsEvents);
        return findAll();
    }

    private void handlePagingNavigationChecks(){
        buttonPrevPage.setDisable(pageNumber == 0);
        buttonNextPage.setDisable((pageNumber + 1) * pageSize >= totalNumberOfElements);
    }

    private void guiInit(){
        comboBoxElems.getItems().addAll(1,3,5,10);
        comboBoxElems.getSelectionModel().select(2);
        comboBoxElems.setOnAction(event -> {
            pageSize = (Integer) comboBoxElems.getSelectionModel().getSelectedItem();
            pageNumber = 0;
            handlePagingNavigationChecks();
            super.notifyObservers(obsEvents);
        });
        buttonPrevPage.setOnAction(event -> {
            goToPreviousPage();
        });
        buttonNextPage.setOnAction(event -> {
            goToNextPage();
        });
    }



}
