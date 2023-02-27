package com.driver;

public class Order {

    private String id;
    private int deliveryTime;



    public Order(String id, String deliveryTime) {

        this.id = id;
        //The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        String[] dts = deliveryTime.split(":");
        int hour = Integer.parseInt(dts[0]);
        int minute = Integer.parseInt(dts[1]);
        this.deliveryTime = minute + (60 * hour);

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
