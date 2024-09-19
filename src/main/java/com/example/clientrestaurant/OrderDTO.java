package com.example.clientrestaurant;

import java.time.LocalDateTime;
import java.util.Map;

public class OrderDTO {
    private int idBuy;
    private Map<String, Integer> dishes;
    private String orderNotes;
    private String orderDateTime;

    public OrderDTO(int idBuy, Map<String, Integer> dishes, String orderNotes, String orderDateTime) {
        this.idBuy = idBuy;
        this.dishes = dishes;
        this.orderNotes = orderNotes;
        this.orderDateTime = orderDateTime;
    }

    public int getIdBuy() {
        return idBuy;
    }

    public void setIdBuy(int idBuy) {
        this.idBuy = idBuy;
    }

    public Map<String, Integer> getDishes() {
        return dishes;
    }

    public void setDishes(Map<String, Integer> dishes) {
        this.dishes = dishes;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(idBuy).append("\n");
        sb.append("Order Date and Time: ").append(orderDateTime).append("\n");

        // Форматируем список блюд и их количество
        sb.append("Dishes:\n");
        for (Map.Entry<String, Integer> entry : dishes.entrySet()) {
            sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        // Добавляем примечания к заказу
        sb.append("Order Notes: ").append(orderNotes.isEmpty() ? "None" : orderNotes).append("\n");

        return sb.toString();
    }
}

