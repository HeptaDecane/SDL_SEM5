package io.sockets;

import java.io.Serializable;

public class Data implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;

    private String firstName;
    private String lastName;
    private int age;

    public Data(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }
}
