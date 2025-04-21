package com.jiawa.wiki.req;

import jakarta.validation.constraints.NotNull;

public class DocSaveReq {
    private Long id;

    @NotNull(message = "Ebook id can not be null")
    private Long ebookId;

    @NotNull(message = "Parent id can not be null")
    private Long parent;

    @NotNull(message = "Name can not be null")
    private String name;

    @NotNull(message = "Order can not be null")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    @NotNull(message = "Content can not be null")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public @NotNull(message = "Content can not be null") String getContent() {
        return content;
    }

    public void setContent(@NotNull(message = "Content can not be null") String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", content='" + content + '\'' +
                '}';
    }
}