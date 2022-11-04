package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.vo.Price;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "menu_name", nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order order, final String name, final Price price, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void arrangeOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public static class Builder {

        private Long seq;
        private Order order;
        private String name;
        private Price price;
        private long quantity;

        public Builder seq(final Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder order(final Order order) {
            this.order = order;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final Price price) {
            this.price = price;
            return this;
        }

        public Builder quantity(final Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, order, name, price, quantity);
        }
    }
}
