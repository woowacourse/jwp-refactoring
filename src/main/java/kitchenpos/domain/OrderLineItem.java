package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.exception.OrderLineItemException.InvalidMenuException;
import org.springframework.lang.NonNull;

@Entity
public class OrderLineItem {

    private static final int LOWER_BOUND_MENU_ID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Order order;
    @Column
    @NonNull
    private Long menuId; // 애그리거트 분리를 위해 id 참조
    @Column
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final Long menuId, final long quantity) {
        validateMenuId(menuId);
        return new OrderLineItem(menuId, quantity);
    }

    private static void validateMenuId(Long menuId) {
        if (menuId == null || menuId < LOWER_BOUND_MENU_ID) {
            throw new InvalidMenuException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
