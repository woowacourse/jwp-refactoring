package kitchenpos.menu;

import kitchenpos.product.Price;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "menu_product")
@Entity
public class MenuProduct {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    private Long productId;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(long quantity, Long productId) {
        this.seq = null;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Price multiplyPrice(Price price) {
        return price.multiplyWithQuantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
