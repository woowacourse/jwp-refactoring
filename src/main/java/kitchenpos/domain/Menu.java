package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(
            String name,
            Price price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(
            Long id,
            String name,
            Price price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(
            String name,
            Long price,
            MenuGroup menuGroup
    ) {
        return new Menu(
                name,
                Price.from(price),
                menuGroup,
                new ArrayList<>()
        );
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.registerMenu(this);
    }

    public void addAllMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
        validateTotalPrice();
    }

    private void validateTotalPrice() {
        Price totalPrice = menuProducts
                .stream()
                .map(MenuProduct::totalPrice)
                .reduce(Price.ZERO, Price::add);

        if (price.moreExpensiveThan(totalPrice)) {
            throw new IllegalArgumentException();
        }
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
