package kitchenpos.domain.menu;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;
    private String name;
    private Price price;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final String name, final Price price, final long quantity) {
        this(null, productId, name,price,quantity);
    }

    public MenuProduct(Long seq, Long productId, String name, Price price, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

}
