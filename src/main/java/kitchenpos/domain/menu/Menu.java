package kitchenpos.domain.menu;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.exception.KitchenposException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.exception.ExceptionInformation.MENU_PRICE_IS_NULL;
import static kitchenpos.exception.ExceptionInformation.MENU_PRICE_OVER_MENU_PRODUCT_PRICE;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MenuGroup menuGroup;

    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final Long id, final Name name, final Price price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(final Name name, final Price price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public static Menu from(final String name, final BigDecimal price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        validateMenuPriceIsNotNull(price);
        validateMenuProductsPrice(price, menuProducts);

        return new Menu(
                Name.from(name),
                Price.from(price),
                menuGroup,
                menuProducts
        );
    }

    private static void validateMenuPriceIsNotNull(final BigDecimal price) {
        if(Objects.isNull(price)){
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


    public MenuGroup getMenuGroup() {
        return menuGroup;
    }


    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
