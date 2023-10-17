package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public static class MenuProductBuilder {

        private Menu menu;
        private Product product;
        private long quantity;

        public MenuProductBuilder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public MenuProductBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(menu, product, quantity);
        }
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
