package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exceptions.InvalidMenuPriceException;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePriceLessThanMenuProductsPrice(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validatePriceLessThanMenuProductsPrice(final Price price, final List<MenuProduct> menuProducts) {
        final Price menuProductsPrice = menuProducts.stream()
                .map(MenuProduct::calculateProductTotalPrice)
                .reduce(new Price(BigDecimal.ZERO), Price::add);
        if (price.isGreaterThan(menuProductsPrice)) {
            throw new InvalidMenuPriceException();
        }
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
