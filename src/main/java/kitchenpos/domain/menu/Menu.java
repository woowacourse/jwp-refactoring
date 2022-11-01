package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts;

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public static Menu ofNew(final String name,
                             final BigDecimal price,
                             final Long menuGroupId) {
        return new Menu(null, name, price, menuGroupId);
    }

    public void changeMenuProducts(final List<MenuProduct> rawMenuProducts) {
        final MenuProducts menuProducts = new MenuProducts(rawMenuProducts);
        if (this.isExpensiveThan(menuProducts.calculateTotalPrice())) {
            throw new IllegalArgumentException();
        }
        this.menuProducts = menuProducts;
    }

    public boolean isExpensiveThan(final Price price) {
        return this.price.isOver(price);
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
        return menuProducts.getValues();
    }

    protected Menu() {
    }
}
