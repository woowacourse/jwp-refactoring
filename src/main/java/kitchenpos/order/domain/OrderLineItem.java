package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class OrderLineItem {

    private static final int MIN_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    private Long menuId;

    @NotNull
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem create(Long menuId, Long quantity) {
        validateMenu(menuId);
        validateQuantity(quantity);

        return new OrderLineItem(menuId, quantity);
    }

    private static void validateMenu(Long menuId) {
        if (menuId == null) {
            throw new NullPointerException("주문 메뉴는 null일 수 없습니다.");
        }
    }

    private static void validateQuantity(Long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("주문 메뉴의 수량은 1개 이상어이야 합니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
