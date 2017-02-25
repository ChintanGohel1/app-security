package com.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

/**
 * Created by Vinit Solanki on 24-Feb-17.
 *  put in url ?pageNumber=1&sort=id&order=asc following by controller
 */
public final class PageRequest implements Pageable {

    @Min(value = 1, message = "Page index must not be less than one!")
    private int pageNumber = 1;

    @Min(value = 1, message = "Page size must not be less than one!")
    private int pageSize = 10;
    private String sort = "id";
    private String order = "ASC";

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int getOffset() {
        return this.pageNumber * this.pageSize;
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.fromString(this.order), this.sort);
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber - 1;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
