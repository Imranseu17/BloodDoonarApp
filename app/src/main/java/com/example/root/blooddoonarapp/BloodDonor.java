package com.example.root.blooddoonarapp;

import java.util.ArrayList;

/**
 * Created by root on 11/5/17.
 */

public class BloodDonor extends ArrayList<BloodDonor> {
    private int id;
    private  String name;
    private  String group;
    private String number;
    private byte[] image;



    public BloodDonor(int id, String name, String group, String number, byte[] image) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.number = number;
        this.image = image;
    }

    public BloodDonor() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
