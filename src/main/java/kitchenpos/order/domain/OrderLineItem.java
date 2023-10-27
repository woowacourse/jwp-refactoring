package kitchenpos.order.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Embedded
    private OrderMenu orderMenu;

    @NotNull
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(
            final Long seq,
            final Order order,
            final Long menuId,
            final String menuName,
            final BigDecimal menuPrice,
            final Long quantity
    ) {
        validateQuantity(quantity);
        this.seq = seq;
        this.order = order;
        this.orderMenu = OrderMenu.of(menuId, menuName, menuPrice);
        this.quantity = quantity;
    }

    public static OrderLineItem create(
            final Order order,
            final Long menuId,
            final String menuName,
            final BigDecimal menuPrice,
            final Long quantity
    ) {
        return new OrderLineItem(null, order, menuId, menuName, menuPrice, quantity);
    }

    private void validateQuantity(final Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException("주문 항목의 수량은 null이 될 수 없습니다.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("주문 항목의 수량은 0개 이상이어야 합니다.");
        }
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }
}
