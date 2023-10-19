package kitchenpos.domain.menu;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Menu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    @OneToOne(fetch = LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(final List<MenuProduct> toAddMenuProducts) {
        this.menuProducts.addAll(toAddMenuProducts);
    }
}
