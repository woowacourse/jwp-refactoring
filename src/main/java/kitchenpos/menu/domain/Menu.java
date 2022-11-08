package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuProductAmountException;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Price price;
    private String name;
    private Long menuGroupId;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(final Long id, final Price price, final String name, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        if (price.isExpansiveThan(calculateAmountSum(menuProducts))) {
            throw new InvalidMenuPriceException();
        }
        this.id = id;
        this.price = price;
        this.name = name;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private static Price calculateAmountSum(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(Price::add)
                .orElseThrow(MenuProductAmountException::new);
    }

    public Menu(final String name, final Price price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this(null, price, name, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }
}
