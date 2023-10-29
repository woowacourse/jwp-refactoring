package kitchenpos.order.domain;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_history_to_order_id"))
    private Long orderId;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected MenuHistory() {
    }

    public MenuHistory(final Long orderId,
                       final Name name,
                       final Price price
    ) {
        this(null, orderId, name, price);
    }

    protected MenuHistory(final Long id,
                          final Long orderId,
                          final Name name,
                          final Price price
    ) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
    }

    public static MenuHistory of(final Long orderId, final Name name, final Price price) {
        return new MenuHistory(
                orderId,
                name,
                price
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
