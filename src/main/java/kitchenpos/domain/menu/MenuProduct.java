package kitchenpos.domain.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.support.domain.BaseEntity;
import kitchenpos.support.money.Money;

@Entity
public class MenuProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private String name;
    private Money price;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, String name, Money price, long quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Long productId, String name, Money price, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Money calculateAmount() {
        return price.times(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
