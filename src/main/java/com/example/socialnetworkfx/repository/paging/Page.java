package com.example.socialnetworkfx.repository.paging;

import java.util.List;

public class Page<E> {

    private List<E> elementsOnPage;
    private int totalNumberOfElements;

    public Page(List<E> elementsOnPage, int totalNumberOfElements) {
        this.elementsOnPage = elementsOnPage;
        this.totalNumberOfElements = totalNumberOfElements;
    }

    public List<E> getElementsOnPage() {
        return elementsOnPage;
    }

    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    @Override
    public String toString() {
        return "Page{" +
                "elementsOnPage=" + elementsOnPage +
                ", totalNumberOfElements=" + totalNumberOfElements +
                '}';
    }
}
