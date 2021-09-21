package com.task.listingtask.model;

public class CommentListModel {
    private String title;
    private String comment;

    public CommentListModel() {
    }

    public CommentListModel(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
