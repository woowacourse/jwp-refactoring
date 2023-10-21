package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.MenuException.PriceMoreThanProductsException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Embedded
    private Price price;
    @ManyToOne
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(final String name,
                 final Price price,
                 final MenuGroup menuGroup,
                 final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        Price menuPrice = Price.from(price);
        validateTotalPrice(menuPrice, menuProducts);

        return new Menu(name, menuPrice, menuGroup, menuProducts);
    }

    private static void validateTotalPrice(final Price price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getMenuProductPrice());
        }

        if (price.isBiggerThan(sum)) {
            throw new PriceMoreThanProductsException(price.getValue(), sum);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public void setPrice(final BigDecimal price) {
        this.price = Price.from(price);
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(final MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }
}
