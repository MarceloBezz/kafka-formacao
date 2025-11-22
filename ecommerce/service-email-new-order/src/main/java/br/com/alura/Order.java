package br.com.alura;

import java.math.BigDecimal;

public class Order {
    private final String orderId, email;
    
    private final BigDecimal amount;
    
    public Order(String orderId, BigDecimal amount, String email) {
        this.orderId = orderId;
        this.email = email;
        this.amount = amount;
    }
    
    public String getEmail() {
        return email;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return """
                Order{
                orderId=%s
                amount=%d
                email=%s
                }""".formatted(orderId, amount.intValue(), email);
    }
}