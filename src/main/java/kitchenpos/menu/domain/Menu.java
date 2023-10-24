package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {

    private static final int MAX_NAME_LENGTH = 255;
    private static final int MIN_PRICE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(
            String name,
            BigDecimal price,
            MenuGroup menuGroup,
            MenuProducts menuProducts,
            MenuValidator menuValidator
    ) {
        validate(name, price, menuGroup);
        menuValidator.validateMenuProducts(menuProducts, price);

        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(String name, BigDecimal price, MenuGroup menuGroup) {
        validateName(name);
        validatePrice(price);
        validateMenuGroup(menuGroup);
    }

    public static Menu create(
            String name,
            BigDecimal price,
            MenuGroup menuGroup,
            MenuProducts menuProducts,
            MenuValidator menuValidator
    ) {
        return new Menu(name, price, menuGroup, menuProducts, menuValidator);
    }

    private void validateName(String name) {
        if (name == null) {
            throw new NullPointerException("메뉴 이름은 null일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("메뉴 이름의 길이는 1글자 이상, 255글자 이하여야 합니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new NullPointerException("메뉴 금액은 null일 수 없습니다.");
        }
        if (price.doubleValue() < MIN_PRICE) {
            throw new IllegalArgumentException("메뉴 금액은 0원 이상이어야 합니다.");
        }
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new NullPointerException("메뉴 그룹은 null일 수 없습니다.");
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
        return menuProducts.getMenuProducts();
    }

}
