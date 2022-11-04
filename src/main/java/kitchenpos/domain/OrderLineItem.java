package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import kitchenpos.vo.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    private String menuName;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "price", column = @Column(name = "menu_price", columnDefinition = "DECIMAL(19, 2) NOT NULL")))
    private Price menuPrice;

    @NotNull
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final String menuName, final Price menuPrice,
                         final long quantity) {
        this.seq = seq;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getPrice();
    }

    public long getQuantity() {
        return quantity;
    }
}
