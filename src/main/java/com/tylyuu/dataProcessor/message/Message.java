package com.tylyuu.dataProcessor.message;

public class Message {
    private String content;
    private int priority;

    public Message () {}

    public Message(String s, int i) {
        this.content = s;
        this.priority = i;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public int getPriority() {
        return priority;
    }
}
