package com.example.clientrestaurant;
public class Payment {
    private int id;                // Уникальный идентификатор платежа
    private int orderId;           // Идентификатор заказа
    private double amount;         // Сумма платежа
    private String paymentMethod;  // Метод оплаты (например, "Карта", "Наличные", "Онлайн")
    private String paymentStatus;  // Статус платежа (например, "Ожидает", "Оплачен", "Отменен")
    private String paymentDate;    // Дата проведения платежа

    public Payment(int id, int orderId, double amount, String paymentMethod, String paymentStatus, String paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void updateStatus(String newStatus) {
        this.paymentStatus = newStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                '}';
    }
}
