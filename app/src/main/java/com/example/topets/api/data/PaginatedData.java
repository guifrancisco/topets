package com.example.topets.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaginatedData<T> {

    @SerializedName("content")
    private List<T> items;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    /**
     *
     * @return the list of elements on this page
     */
    public List<T> getItems() {
        return items;
    }
    /**
     *
     * @return true if this is the last page, false otherwise
     */
    public boolean isLast() {
        return last;
    }

    /**
     * Returns the amount of elements in the resource repository. This does not represent the amount
     * of elements on this page, for that use {@link PaginatedData#getNumberOfElements()}
     * @return the total amount of elements in the resource repository.
     */
    public int getTotalElements() {
        return totalElements;
    }

    /**
     *
     * @return the total amount of pages in the resource repository.
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @return the size of this page, that is, the maximum amount of elements this page can hold.
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @return true if this is the first page, false otherwise
     */
    public boolean isFirst() {
        return first;
    }

    /**
     *
     * @return the amount of elements present on this page
     */
    public int getNumberOfElements() {
        return numberOfElements;
    }

    /**
     *
     * @return true if this page is empty, false otherwise.
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     *
     * @return the index of the current page.
     */
    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "PaginatedData{" +
                "items=" + items +
                ", last=" + last +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", size=" + size +
                ", number=" + number +
                ", first=" + first +
                ", numberOfElements=" + numberOfElements +
                ", empty=" + empty +
                '}';
    }
}
