package kitchenpos.menu.domain;

import kitchenpos.generic.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Menu menu;

    @OneToOne
    private Product product;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, Long quantity) {
        this(product, quantity);
        placeMenu(menu);
    }

    public MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculateSum() {
        return product.calculatePrice(quantity);
    }

    public void placeMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("등록할 메뉴가 없습니다.");
        }
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

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

}
