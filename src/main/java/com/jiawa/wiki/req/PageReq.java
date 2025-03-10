package com.jiawa.wiki.req;

public class PageReq {
    private int pages;

    private int size;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
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
                "pages=" + pages +
                ", size=" + size +
                '}';
    }
}