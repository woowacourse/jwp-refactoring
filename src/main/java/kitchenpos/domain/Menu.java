package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
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

    public Menu(final Long id, final String name, final BigDecimal price, final long menuGroupId) {
        this(id, name, price, menuGroupId, PendingMenuProducts.empty());
    }

    Menu(final String name, final BigDecimal price, final long menuGroupId, final PendingMenuProducts products) {
        this(null, name, price, menuGroupId, products);
    }

    private Menu(final Long id, final String name, final BigDecimal price, final long menuGroupId, final PendingMenuProducts products) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(products.getTotalPrice()) > 0) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = products.createMenuProducts();
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
