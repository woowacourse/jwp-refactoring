package kitchenpos.menu;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.product.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long productId;

    @Embedded
    private Price price;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, int quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(Long id, Product product, int quantity) {
        this.id = id;
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = new Quantity(quantity);
    }

    public BigDecimal calculateTotalPrice() {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getProductId() {
        return productId;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
