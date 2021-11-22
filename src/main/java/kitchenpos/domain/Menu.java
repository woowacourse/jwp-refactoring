package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
                 List<MenuProduct> menuProducts) {
        validatesPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validateMenuProductsPrice();
    }

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public static Menu create(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    private void validatesPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuProductsPrice() {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (price.compareTo(sum) > 0) {
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
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
