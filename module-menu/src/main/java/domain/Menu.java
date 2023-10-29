package domain;

import exception.MenuException;
import java.util.Objects;
import java.util.function.Supplier;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import domain.menu_product.MenuProducts;
import support.AggregateReference;
import vo.Price;

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
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "menu_group_id"))
    private final AggregateReference<MenuGroup> menuGroupId;

    protected Menu() {
        this.id = null;
        this.menuName = null;
        this.menuPrice = null;
        this.menuProducts = null;
        this.menuGroupId = null;
    }

    public Menu(
            final MenuName menuName,
            final Price menuPrice,
            final MenuProducts menuProducts,
            final AggregateReference<MenuGroup> menuGroupId,
            final MenuValidator menuValidator
    ) {
        this.id = null;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuProducts = menuProducts;
        this.menuGroupId= menuGroupId;
        validateCreate(menuName, menuPrice, menuProducts, menuGroupId);
        menuValidator.validate(this);
    }

    private void validateCreate(
            final MenuName menuName,
            final Price menuPrice,
            final MenuProducts menuProducts,
            final AggregateReference<MenuGroup> menuGroup
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

    public AggregateReference<MenuGroup> getMenuGroup() {
        return menuGroupId;
    }
}
