package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.dto.vo.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @Embedded
    private Quantity quantity;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private BigDecimal menuPrice;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long menuId, long quantity, String menuName, BigDecimal menuPrice) {
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public static class OrderLineItemBuilder {

        private Long menuId;
        private long quantity;
        private String menuName;
        private BigDecimal price;

        public OrderLineItemBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItemBuilder menuName(String name) {
            this.menuName = name;
            return this;
        }

        public OrderLineItemBuilder price(String price) {
            this.price = BigDecimal.valueOf(Long.parseLong(price));
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(menuId, quantity, menuName, price);
        }
    }
}
