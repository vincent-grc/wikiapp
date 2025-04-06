package com.jiawa.wiki.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class PageReq {
    @NotNull(message = "Page number can not be null")
    private int page;

    @NotNull(message = "Page size can not be null")
    @Max(value = 1000, message = "Page size can not be greater than 1000")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int pages) {
        this.page = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}