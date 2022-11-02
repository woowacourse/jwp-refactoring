package kitchenpos.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long menuId;
    private BigDecimal menuPrice;
    private String menuName;
    private long quantity;

    public OrderLineItemDto() {
    }

    public OrderLineItemDto(final Long menuId, final BigDecimal menuPrice, final String menuName, final long quantity) {
        this.menuId = menuId;
        this.menuPrice = menuPrice;
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public static List<OrderLineItemDto> from(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemDto::from)
                .collect(Collectors.toList());
    }

    private static OrderLineItemDto from(final OrderLineItem orderLineItem) {
        return new OrderLineItemDto(
                orderLineItem.getMenuId(),
                orderLineItem.getMenuPrice(),
                orderLineItem.getMenuName(),
                orderLineItem.getQuantity()
        );
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity, menuPrice, menuName);
    }
}
