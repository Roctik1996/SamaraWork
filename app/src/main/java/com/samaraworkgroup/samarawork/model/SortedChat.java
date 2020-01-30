package com.samaraworkgroup.samarawork.model;

import java.util.Comparator;

public class SortedChat implements Comparator<ChatList> {
    @Override
    public int compare(ChatList o1, ChatList o2) {
        return (int)(o2.getLastMessage()-o1.getLastMessage());
    }
}