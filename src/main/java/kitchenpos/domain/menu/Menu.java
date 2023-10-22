package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(
            final Long id,
            final String name,
            final Price price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup
    ) {
        this(null, name, Price.from(price), menuGroup, new ArrayList<>());
    }

    public void applyMenuProducts(final List<MenuProduct> menuProducts, final Price totalPrice) {
        validateMenuProductsPrice(totalPrice);
        this.menuProducts = menuProducts;
    }

    private void validateMenuProductsPrice(final Price totalPrice) {
        if (this.price.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("Sum of menu products price must be greater than menu price.");
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
