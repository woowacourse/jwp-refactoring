package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts;

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new ArrayList<>();
    }

    public static Menu ofNew(final String name,
                             final BigDecimal price,
                             final Long menuGroupId) {
        return new Menu(null, name, price, menuGroupId);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
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
        return menuProducts;
    }

    protected Menu() {
    }
}
