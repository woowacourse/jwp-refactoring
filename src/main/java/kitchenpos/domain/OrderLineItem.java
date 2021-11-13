package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidMenuProductException;
import kitchenpos.exception.InvalidOrderLineItemException;

//TODO: 테스트 코드 작성
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @NotNull
    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, Long quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(Long seq, Order order, Menu menu, Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
        validateNull(this.order);
        validateNull(this.menu);
        validateQuantity(this.quantity);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InvalidOrderLineItemException("OrderLineItem 정보에 null이 포함되었습니다.");
        }
    }

    private void validateQuantity(Long quantity) {
        validateNull(quantity);
        if (quantity < 0) {
            throw new InvalidOrderLineItemException(String.format("음수 %s는 개수가 될 수 없습니다.", quantity));
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
