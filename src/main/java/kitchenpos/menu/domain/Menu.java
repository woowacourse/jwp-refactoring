package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "menu_product",
            joinColumns = @JoinColumn(name = "menuId")
    )
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(final String name, final BigDecimal price, final long menuGroupId, final List<MenuProduct> products) {
        this.id = null;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = products;
    }

    public static Menu create(
            final String name, final BigDecimal price, final PendingMenuProducts products, final long menuGroupId
    ) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(products.getTotalPrice()) > 0) {
            throw new IllegalArgumentException();
        }
        return new Menu(name, price, menuGroupId, products.createMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
