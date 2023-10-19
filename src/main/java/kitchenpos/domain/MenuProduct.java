package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        this(null, null, product, quantity);
    }

    public MenuProduct(Menu menu, Product product, Quantity quantity) {
        this(null, menu, product, quantity);
    }

    public MenuProduct(
            Long seq,
            Menu menu,
            Product product,
            Quantity quantity
    ) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(
            Product product,
            long quantity
    ) {
        return new MenuProduct(
                product,
                Quantity.from(quantity)
        );
    }

    public Price totalPrice() {
        BigDecimal quantity = BigDecimal.valueOf(this.quantity.getValue());

        return Price.from(
                product.getPrice()
                        .multiply(quantity)
        );
    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
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

    public long getQuantity() {
        return quantity.getValue();
    }

}
