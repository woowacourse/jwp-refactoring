package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class MenuFixtures {

    private static final long QUANTITY = 1L;
    private static final String MENU_NAME = "기본 메뉴";
    private static final long PRICE = 10000;
    private static final MenuGroup MENU_GROUP = MenuGroupFixtures.createMenuGroup();
    private static final long MENU_PRODUCT_ID = 1L;
    private static final long MENU_ID = 1L;

    public static Menu createMenu(
        Long id,
        String name,
        long price,
        MenuGroup menuGroup,
        List<MenuProduct> menuProducts
    ) {
        return new Menu(id, name, new Price(BigDecimal.valueOf(price)), menuGroup, menuProducts);
    }

    public static Menu createMenu() {
        return createMenu(MENU_ID, MENU_NAME, PRICE, MENU_GROUP, createMenuProducts());
    }

    public static Menu createMenu(int price) {
        return createMenu(MENU_ID, MENU_NAME, price, MENU_GROUP, createMenuProducts());
    }

    public static MenuRequest createMenuRequest() {
        return createMenuRequest(createMenu());
    }

    public static MenuRequest createMenuRequest(Menu menu) {
        return new MenuRequest(
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroup().getId(),
            menu.getMenuProducts().stream()
                .map(menuProduct -> new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity()))
                .collect(Collectors.toList())
        );
    }

    public static MenuProduct createMenuProduct(
        Long id,
        Menu menu,
        Product product,
        long quantity
    ) {
        return new MenuProduct(id, menu, product, quantity);
    }

    public static MenuProduct createMenuProduct() {
        return createMenuProduct(null, null, ProductFixtures.createProduct(), QUANTITY);
    }

    public static List<MenuProduct> createMenuProducts() {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(createMenuProduct());
        menuProducts.add(createMenuProduct());
        return menuProducts;
    }
}
