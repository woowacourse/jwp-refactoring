package kitchenpos.domain.menu;

import java.util.Objects;
import java.util.function.Supplier;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.vo.Price;
import kitchenpos.exception.MenuException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Embedded
    private final MenuName menuName;
    @Embedded
    private final Price menuPrice;
    @Embedded
    private final MenuProducts menuProducts;
    @ManyToOne(fetch = FetchType.LAZY)
    private final MenuGroup menuGroup;

    protected Menu() {
        this.id = null;
        this.menuName = null;
        this.menuPrice = null;
        this.menuProducts = null;
        this.menuGroup = null;
    }

    public Menu(
            final MenuName menuName,
            final Price menuPrice,
            final MenuProducts menuProducts,
            final MenuGroup menuGroup
    ) {
        validateCreate(menuName, menuPrice, menuProducts, menuGroup);
        validateMenuPrice(menuPrice, menuProducts);
        this.id = null;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuProducts = menuProducts;
        this.menuGroup = menuGroup;
    }

    private void validateCreate(
            final MenuName menuName,
            final Price menuPrice,
            final MenuProducts menuProducts,
            final MenuGroup menuGroup
    ) {
        validateNotNull(menuName, MenuException.NoMenuNameException::new);
        validateNotNull(menuGroup, MenuException.NoMenuGroupException::new);
        validateNotNull(menuPrice, MenuException.NoPriceException::new);
        validateNotNull(menuProducts, MenuException.NoMenuProductsException::new);
    }

    private <T> void validateNotNull(T obj, Supplier<MenuException> exceptionSupplier) {
        if (Objects.isNull(obj)) {
            throw exceptionSupplier.get();
        }
    }

    private void validateMenuPrice(final Price menuPrice, final MenuProducts menuProducts) {
        final Price totalPrice = menuProducts.getTotalPrice();
        if(menuPrice.isMoreThan(totalPrice)) {
            throw new MenuException.OverPriceException(totalPrice);
        }
    }

    public Long getId() {
        return id;
    }

    public MenuName getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
