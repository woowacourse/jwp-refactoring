package kitchenpos.menu.domain;

import kitchenpos.global.exception.KitchenposException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.global.exception.ExceptionInformation.MENU_PRICE_IS_NULL;
import static kitchenpos.global.exception.ExceptionInformation.MENU_PRICE_OVER_MENU_PRODUCT_PRICE;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final Long id, final Name name, final Price price, final Long menuGroupId, final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final Name name, final Price price, final Long menuGroupId, final MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu create(final String name, final BigDecimal price, final Long menuGroupId, final MenuProducts menuProducts) {
        validateMenuPriceIsNotNull(price);
        validateMenuProductsPrice(price, menuProducts);

        return new Menu(
                Name.create(name),
                Price.create(price),
                menuGroupId,
                menuProducts
        );
    }

    private static void validateMenuPriceIsNotNull(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new KitchenposException(MENU_PRICE_IS_NULL);
        }
    }

    private static void validateMenuProductsPrice(final BigDecimal price, final MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.getPrice()) > 0) {
            throw new KitchenposException(MENU_PRICE_OVER_MENU_PRODUCT_PRICE);
        }
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name.getName();
    }


    public BigDecimal getPrice() {
        return price.getPrice();
    }


    public Long getMenuGroupId() {
        return menuGroupId;
    }


    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
