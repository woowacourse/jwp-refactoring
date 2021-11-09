package kitchenpos.domain;

public class OrderDetail {
    private Long orderId;
    private Menu menu;
    private Integer quantity;

    public OrderDetail(Long orderId, Menu menu, Integer quantity) {
        this.orderId = orderId;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
