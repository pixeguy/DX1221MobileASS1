package com.example.sampleapp.PostOffice;

public class MessageEndGame extends Message{
    public enum END_CONDITION {
        LOOTING_PHASE,
        LOSE_PHASE
    }

    public END_CONDITION condition;

    public MessageEndGame(END_CONDITION cond) {condition = cond;}
}
