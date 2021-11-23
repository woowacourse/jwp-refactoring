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
            throw new IllegalArgumentException("Menu의 price는 null이 아니거나 0보다 커야합니다.");
        }
    }

    private void validateMenuProductsPrice() {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("상품들의 총합 가격보다 메뉴의 가격이 높으면 안됩니다.");
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

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
