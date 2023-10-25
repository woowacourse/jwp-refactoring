package kitchenpos.domain.menu;

import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "menu_product")
@Entity
public class MenuProduct {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @ManyToOne
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.seq = null;
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculateTotalPrice() {
        return product.multiplyWithQuantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
