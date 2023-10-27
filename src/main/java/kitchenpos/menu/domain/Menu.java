package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @Embedded
    private MenuProducts menuProducts;

    private Long menuGroupId;

    protected Menu() {
    }

    public Menu(final Long id, final String name, final List<MenuProduct> menuProducts, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.menuProducts = new MenuProducts(menuProducts);
        this.price = this.menuProducts.calculatePrice();
        this.menuGroupId = menuGroupId;
    }

    public static Menu forSave(final String name, final List<MenuProduct> menuProducts, final Long menuGroupId) {
        return new Menu(null, name, menuProducts, menuGroupId);
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

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts.getMenuProducts());
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public boolean hasSamePrice(final int price) {
        final Price givenPrice = new Price(BigDecimal.valueOf(price));

        return this.price.equals(givenPrice);
    }
}
