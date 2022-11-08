package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Menu {

    @Id
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;

    @MappedCollection(idColumn = "MENU_ID")
    private final List<MenuProduct> menuProducts;

    private Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name, final BigDecimal price, final Long menuGroupId,
                          final List<MenuProduct> menuProducts, final MenuProductService menuProductService) {
        menuProductService.verifyAllMenuProductExist();
        verifyPrice(price, menuProducts, menuProductService);
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    private static void verifyPrice(final BigDecimal price, final List<MenuProduct> menuProducts,
                                    final MenuProductService menuProductService) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        if (menuProductService.isGraterThanTotalAmount(price)) {
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
