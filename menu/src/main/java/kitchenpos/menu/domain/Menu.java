package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Menu {

    @Id
    private Long id;
    private String name;
    @Embedded.Empty
    private Price price;
    private Long menuGroupId;
    @MappedCollection(idColumn = "MENU_ID")
    private List<MenuProduct> menuProducts;

    private Menu() {
    }

    private Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static Menu create(
            String name,
            Price price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts,
            List<Product> products
    ) {
        Price totalPrice = MenuProductPriceCalculator.calculateTotalPrice(menuProducts, products);
        validatePrice(price, totalPrice);

        return new Menu(null, name, price, menuGroup.getId(), menuProducts);
    }

    private static void validatePrice(Price price, Price totalPrice) {
        if (price.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
