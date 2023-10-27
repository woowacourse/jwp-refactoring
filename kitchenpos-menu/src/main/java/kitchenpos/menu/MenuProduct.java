package kitchenpos.menu;

import kitchenpos.common.Price;
import kitchenpos.product.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Product product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Price evaluate() {
        return product.getPrice().multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
