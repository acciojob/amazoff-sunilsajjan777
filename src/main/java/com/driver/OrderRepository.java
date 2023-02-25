package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository

public class OrderRepository {

    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();
    HashMap<DeliveryPartner, List<Order>> deliveryPartnerOrderListDb = new HashMap<>();

    public void addOrderFromDb(Order order) {
        orderDb.put(order.getId(), order);
    }


    public void addPartnerFromDB(String partnerId) {
        deliveryPartnerDb.put(partnerId, new DeliveryPartner(partnerId));
    }


    public DeliveryPartner getPartnerByIdFromDB(String partnerId) {
        return deliveryPartnerDb.getOrDefault(partnerId, null);
    }

    public Order getOrderByIdFromDb(String orderId) {
        return orderDb.getOrDefault(orderId, null);
    }

    public void addOrderPartnerPairFromDb(String orderId, String partnerId) {
        Order order = getOrderByIdFromDb(orderId);
        if (deliveryPartnerOrderListDb.containsKey(getPartnerByIdFromDB(partnerId))) {
            List<Order> orderList = deliveryPartnerOrderListDb.get(getPartnerByIdFromDB(partnerId));
            orderList.add(order);
            deliveryPartnerOrderListDb.put(getPartnerByIdFromDB(partnerId), orderList);
        } else {
            List<Order> orderList = new ArrayList<>();
            orderList.add(order);
            deliveryPartnerOrderListDb.put(getPartnerByIdFromDB(partnerId), orderList);
        }
        int orders = getPartnerByIdFromDB(partnerId).getNumberOfOrders();
        deliveryPartnerDb.get(partnerId).setNumberOfOrders(orders + 1);

    }

    public Integer getOrderCountByPartnerIdFromDb(String partnerId) {
        return deliveryPartnerDb.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerIdFromDb(String partnerId) {
        List<Order> orderList = new ArrayList<>();
        orderList = deliveryPartnerOrderListDb.get(getPartnerByIdFromDB(partnerId));
        List<String> orders = new ArrayList<>();
        for (Order order : orderList) {
            orders.add(order.getId());
        }
        return orders;
    }

    public List<String> getAllOrdersFromDb() {
        List<String> allOrderList = new ArrayList<>();
        for (Map.Entry<String, Order> set : orderDb.entrySet()) {
            allOrderList.add(set.getKey());
        }
        return allOrderList;
    }

    public Integer getCountOfUnassignedOrdersFromDb() {
        int totalOrders = orderDb.size();
        int assignedOrders = 0;
        for (Map.Entry<DeliveryPartner, List<Order>> set : deliveryPartnerOrderListDb.entrySet()) {
            List<Order> orders = set.getValue();
            assignedOrders = assignedOrders + orders.size();
        }
        return totalOrders - assignedOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerIdFromDb(String time, String partnerId) {
        int countOfOrders = 0;
        List<Order> orderList = new ArrayList<>();
        orderList = deliveryPartnerOrderListDb.getOrDefault(getPartnerByIdFromDB(partnerId), null);
        int timeInInt;
        String[] dts = time.split(":");
        int hour = Integer.parseInt(dts[0]);
        int minute = Integer.parseInt(dts[1]);
        timeInInt = minute + (60 * hour);
        for (Order order : orderList) {
            if (order.getDeliveryTime() > timeInInt) {
                countOfOrders = countOfOrders + 1;
            }
        }

        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerIdFromDb(String partnerId) {
        List<Order> orderList = new ArrayList<>();
        orderList = deliveryPartnerOrderListDb.getOrDefault(getPartnerByIdFromDB(partnerId), null);

        int time = 0;
        for (Order order : orderList) {
            time = Math.max(time, order.getDeliveryTime());
        }
        Order order = orderList.get(orderList.size() - 1);
        String timeInString = "";
        int h = time / 60;
        int m = time % 60;
        String hour = Integer.toString(h);
        String minutes = Integer.toString(m);
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        timeInString = hour + ":" + minutes;
        return timeInString;
    }

    public void deletePartnerByIdFromDb(String partnerId) {
        DeliveryPartner deliveryPartner = getPartnerByIdFromDB(partnerId);
        deliveryPartnerOrderListDb.remove(deliveryPartner);
        deliveryPartnerDb.remove(partnerId);
    }

    public void deleteOrderByIdFromDb(String orderId) {
        for (Map.Entry<DeliveryPartner, List<Order>> set : deliveryPartnerOrderListDb.entrySet()) {
            List<Order> orders = set.getValue();
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                if (order.getId().equals(orderId)) {
                    orders.remove(order);
                    deliveryPartnerOrderListDb.put(set.getKey(), orders);
                    break;
                }

            }

        }
        orderDb.remove(orderId);

    }
}