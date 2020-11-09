package kitchenpos.domain;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long tableId;
    private String orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long tableId) {
        this.tableId = tableId;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(final Long tableId) {
        this.tableId = tableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }
}
