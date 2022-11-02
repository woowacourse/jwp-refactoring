package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, Long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Price getProductsPrice() {
        product.getPrice().multiply(quantity);
        return product.getPrice();
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
