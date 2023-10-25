package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.REMOVE})
    private List<MenuProduct> menuProducts;

    @Embedded
    private Price price;

    private String name;

    public Menu() {
    }

    public Menu(
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts,
            final Price price,
            final String name
    ) {
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        this.price = price;
        this.name = name;
    }

    public static Menu of(
            final MenuGroup menuGroup,
            final MenuProducts menuProducts,
            final String name,
            final BigDecimal price
    ) {
        validatePrice(menuProducts, price);
        return new Menu(menuGroup, menuProducts.getAll(), new Price(price), name);
    }

    private static void validatePrice(final MenuProducts menuProducts, final BigDecimal price) {
        final Price menuPrice = new Price(price);
        final Price sum = menuProducts.calculateTotalPrice();

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public void setMenuProducts(final List<MenuProduct> savedMenuProducts) {
        this.menuProducts = savedMenuProducts;
    }

    public Long getId() {
        return id;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
