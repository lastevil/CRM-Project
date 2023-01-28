package org.unicrm.chat.model;

public class ChatListUser {
    private Long id;
    private String nicName;
    private int count; //количество уведомлений

    public ChatListUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
