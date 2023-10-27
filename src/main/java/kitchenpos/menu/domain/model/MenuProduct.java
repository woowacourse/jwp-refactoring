package kitchenpos.menu.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.global.Price;
import kitchenpos.product.domain.model.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(Long seq, Product product, Long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculatePrice() {
        return product.getPrice().multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
