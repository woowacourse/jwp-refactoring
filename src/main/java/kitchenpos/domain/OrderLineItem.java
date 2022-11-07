package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Order order;

    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItem() {}

    public OrderLineItem(final Order order, final String menuName, final BigDecimal menuPrice,
                         final long quantity) {
        this.order = order;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }
}
