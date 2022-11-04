package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "quantity")
    private long quantity;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private BigDecimal menuPrice;

    public OrderLineItem(final Long menuId, final long quantity, final String menuName, final BigDecimal menuPrice) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public OrderLineItem() {
    }
}
