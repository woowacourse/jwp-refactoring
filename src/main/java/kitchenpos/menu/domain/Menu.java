package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.value.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(fetch = LAZY, mappedBy = "menuId")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(
            final String name,
            final Price price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(
            final Long id,
            final String name,
            final Price price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final Menu menu, final List<MenuProduct> menuProducts) {
        return new Menu(menu.id, menu.name, menu.price, menu.menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public BigDecimal getPriceValue() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
