package com.example.sampleapp.PostOffice;

import android.util.Log;

import java.util.HashMap;

public class PostOffice {
    private static PostOffice instance;
    private final HashMap<String, ObjectBase> m_addressBook = new HashMap<>();

    public static PostOffice getInstance() {
        if (instance == null) {
            instance = new PostOffice();
        }
        return instance;
    }

    public void register(String address, ObjectBase object) {
        if(object == null) return;
        if(m_addressBook.containsKey(address)) return;
        m_addressBook.put(address, object);
    }

    public boolean send(String address, Message message) {
        if(message == null) return false;
        if (!m_addressBook.containsKey(address)) return false;
        ObjectBase object = m_addressBook.get(address);
        if(object == null) return false;
        return object.handle(message);
    }

    void broadcast(Message message) {
        if(message == null) return;
        for (ObjectBase object : m_addressBook.values()) {
            object.handle(message);
        }
    }

    void groupcast(String[] addresses, Message message) {
        if(message == null) return;
        for (String address : addresses) {
            if (!m_addressBook.containsKey(address)) continue;
            ObjectBase object = m_addressBook.get(address);
            if(object == null) continue;
            object.handle(message);
        }
    }

    public void clear() {
        m_addressBook.clear();
    }
}
