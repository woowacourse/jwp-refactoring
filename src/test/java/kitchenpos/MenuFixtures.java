package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixtures {

    private static final String NAME = "메뉴";
    private static final int PRICE = 1000;
    private static final long MENU_GROUP_ID = 1L;

    private static final int QUANTITY = 3;
    private static final long MENU_ID = 1L;
    private static final long PRODUCT_ID = 1L;

    private MenuFixtures() {
    }

    public static Menu createMenu() {
        return createMenu(PRICE, MENU_GROUP_ID, List.of(createMenuProduct(), createMenuProduct()));
    }

    public static Menu createMenu(int price) {
        return createMenu(price, MENU_GROUP_ID, List.of(createMenuProduct(), createMenuProduct()));
    }

    public static Menu createMenu(long menuGroupId) {
        return createMenu(PRICE, menuGroupId, List.of(createMenuProduct(), createMenuProduct()));
    }

    public static Menu createMenu(int price, long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(
                NAME,
                BigDecimal.valueOf(price),
                menuGroupId,
                menuProducts
        );
    }

    public static MenuProduct createMenuProduct() {
        return createMenuProduct(MENU_ID, ProductFixtures.createProduct(), QUANTITY);
    }

    public static MenuProduct createMenuProduct(long menuId, Product product, int quantity) {
        return new MenuProduct(menuId, product, quantity);
    }

    public static MenuCreateRequest createMenuCreateRequest() {
        return new MenuCreateRequest(
                "메뉴",
                BigDecimal.valueOf(30000),
                1L,
                List.of(new MenuProductCreateRequest(null, 1L, 2))
        );
    }

    public static MenuCreateRequest createMenuCreateRequest(BigDecimal price) {
        return createMenuCreateRequest(price, MENU_GROUP_ID, List.of(createMenuProductCreateRequest()));
    }

    public static MenuCreateRequest createMenuCreateRequest(long menuGroupId) {
        return createMenuCreateRequest(BigDecimal.valueOf(PRICE), menuGroupId, List.of(createMenuProductCreateRequest()));
    }

    public static MenuCreateRequest createMenuCreateRequest(List<MenuProductCreateRequest> productRequests) {
        return createMenuCreateRequest(BigDecimal.valueOf(PRICE), MENU_GROUP_ID, productRequests);
    }

    public static MenuCreateRequest createMenuCreateRequest(
            BigDecimal price,
            long menuGroupId,
            List<MenuProductCreateRequest> productCreateRequests
    ) {
        return new MenuCreateRequest("메뉴", price, menuGroupId, productCreateRequests);
    }

    public static MenuProductCreateRequest createMenuProductCreateRequest() {
        return createMenuProductCreateRequest(MENU_ID, PRODUCT_ID, QUANTITY);
    }

    public static MenuProductCreateRequest createMenuProductCreateRequest(Long menuId, Long productId, int quantity) {
        return new MenuProductCreateRequest(menuId, productId, quantity);
    }
}
