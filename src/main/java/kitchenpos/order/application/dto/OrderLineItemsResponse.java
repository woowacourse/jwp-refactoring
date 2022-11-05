package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Price;

public class OrderLineItemsResponse {

    private Long seq;
    private Long orderId;
    private String menuName;
    private Price menuPrice;
    private Long menuQuantity;
    private String productName;
    private Price productPrice;
    private Long productQuantity;

    public OrderLineItemsResponse(OrderLineItem it) {
    }

    public OrderLineItemsResponse(Long seq, Long orderId, String menuName, Price menuPrice, Long menuQuantity,
                                  String productName, Price productPrice, Long productQuantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuQuantity = menuQuantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public Long getMenuQuantity() {
        return menuQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }
}
