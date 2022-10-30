package kitchenpos.domain;

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

    public Menu(final Long id, final String name, final BigDecimal price, final long menuGroupId) {
        this(id, name, price, menuGroupId, List.of());
    }

    Menu(final String name, final BigDecimal price, final long menuGroupId, final List<MenuProduct> products) {
        this(null, name, price, menuGroupId, products);
    }

    private Menu(final Long id, final String name, final BigDecimal price, final long menuGroupId, final List<MenuProduct> products) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = products;
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
