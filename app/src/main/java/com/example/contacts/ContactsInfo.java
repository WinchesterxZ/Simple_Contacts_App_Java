package com.example.contacts;

public class ContactsInfo {
    String name , phoneNumber;
    int imageId ;
    boolean liked;

    public ContactsInfo(String name, String phoneNumber, int imageId, boolean liked) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageId = imageId;
        this.liked = liked;
    }
    public ContactsInfo(String name, String phoneNumber, int imageId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean getLikedState() {
        return liked;
    }

    public void setLikedState(boolean liked) {
        this.liked = liked;
    }
}
