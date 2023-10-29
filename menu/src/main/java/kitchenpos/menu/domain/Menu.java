package kitchenpos.menu.domain;

import kitchenpos.common.domain.Money;
import kitchenpos.common.domain.Name;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Menu {
    public static final int MIN_MENU_PRODUCTS_SIZE = 1;
    public static final String MINIMUM_MENU_PRODUCTS_SIZE_ERROR_MESSAGE = "메뉴에는 1개 이상의 상품이 포함되어야 합니다.";
    public static final String MENU_PRICE_IS_BIGGER_THAN_SUM_ERROR_MESSAGE = "메뉴의 가격은 상품의 가격 합보다 클 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Embedded
    private Name name;
    @NotNull
    @Embedded
    private Money price;
    @NotNull
    private Long menuGroupId;
    @NotNull
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(final Long id, final Name name, final Money price, final Long menuGroupId, final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        validateMenuProductsSize(menuProducts);
        validateMenuPrice(price, menuProducts.calculateSum());
        this.menuProducts = menuProducts;
    }

    public static Menu of(final Name name, final Money price, final Long menuGroupId, final MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(final String name, final long price, final Long menuGroupId, final MenuProducts menuProducts) {
        return new Menu(null, Name.of(name), Money.valueOf(price), menuGroupId, menuProducts);
    }

    private void validateMenuProductsSize(final MenuProducts menuProducts) {
        if (menuProducts.size() < MIN_MENU_PRODUCTS_SIZE) {
            throw new IllegalArgumentException(MINIMUM_MENU_PRODUCTS_SIZE_ERROR_MESSAGE);
        }
    }

    private static void validateMenuPrice(final Money price, final Money sum) {
        if (price.isBiggerThan(sum)) {
            throw new IllegalArgumentException(MENU_PRICE_IS_BIGGER_THAN_SUM_ERROR_MESSAGE);
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

}
