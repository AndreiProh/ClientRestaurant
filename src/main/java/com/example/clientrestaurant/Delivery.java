package com.example.clientrestaurant;
public class Delivery {
    private int id;                    // Уникальный идентификатор доставки
    private int orderId;               // Идентификатор заказа
    private int courierId;             // Идентификатор курьера
    private String deliveryAddress;    // Адрес доставки
    private String deliveryStatus;     // Статус доставки (напр., "Ожидает", "В пути", "Доставлено")
    private String startDeliveryTime;  // Время назначения доставки
    private String deliveryTime;       // Фактическое время доставки
    private String comment;

    public Delivery(int id, int orderId, int courierId, String deliveryAddress, String deliveryStatus, String startDeliveryTime) {
        this.id = id;
        this.orderId = orderId;
        this.courierId = courierId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = deliveryStatus;
        this.startDeliveryTime = startDeliveryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStartDeliveryTime(String startDeliveryTime) {
        this.startDeliveryTime = startDeliveryTime;
    }

    // Метод для обновления статуса доставки
    public void updateStatus(String newStatus) {
        this.deliveryStatus = newStatus;
    }

    public String getStartDeliveryTime() {
        return startDeliveryTime;
    }

    // Переопределение метода toString для отображения информации о доставке
    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", courierId=" + courierId +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", deliveryTime='" + startDeliveryTime + '\'' +
                '}';
    }
}
