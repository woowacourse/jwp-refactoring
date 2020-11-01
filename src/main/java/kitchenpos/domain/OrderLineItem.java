package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    @Builder
    public OrderLineItem(final Long seq, final Menu menu, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void setOrder(final Order order) {
        validateAccessThroughOrder(order);
        this.order = order;
    }

    private void validateAccessThroughOrder(final Order order) {
        if (!order.getOrderLineItems().contains(this)) {
            throw new IllegalStateException();
        }
    }
}
