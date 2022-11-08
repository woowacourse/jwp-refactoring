package kitchenpos.order.dto.application;

import java.math.BigDecimal;

public class OrderLineItemDto {

    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    public OrderLineItemDto(String menuName, BigDecimal menuPrice, long quantity) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }

}
