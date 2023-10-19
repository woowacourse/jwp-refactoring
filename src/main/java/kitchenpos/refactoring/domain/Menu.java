package kitchenpos.refactoring.domain;

import java.util.Collections;
import java.util.List;
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

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(
            Price totalPrice,
            String name,
            Price price,
            Long menuGroupId,
            List<MenuProduct> menuProducts
    ) {
        validatePrice(price, totalPrice);

        return new Menu(name, price, menuGroupId, menuProducts);
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
