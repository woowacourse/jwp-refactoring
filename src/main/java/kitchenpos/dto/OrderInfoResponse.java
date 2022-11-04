package kitchenpos.dto;

public class OrderInfoResponse {

    private Long orderId;
    private Long quantity;
    private Long price;
    private String menuName;

    public OrderInfoResponse(Long orderId, Long quantity, Long price, String menuName) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
        this.menuName = menuName;
    }

    public OrderInfoResponse() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getPrice() {
        return price;
    }

    public String getMenuName() {
        return menuName;
    }
}
