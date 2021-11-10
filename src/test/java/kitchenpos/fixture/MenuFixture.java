package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {
    private static final Long ID = 1L;
    private static final String NAME = "MENU_NAME";
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final Long MENU_GROUP_ID = 1L;

    private static final Long SEQ = 1L;
    private static final Long MENU_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenu() {
        return createMenu(ID, NAME, PRICE, MENU_GROUP_ID, createMenuProducts());
    }

    public static Menu createMenu(BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return createMenu(ID, NAME, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(Long menuGroupId, List<MenuProduct> menuProducts) {
        return createMenu(ID, NAME, PRICE, menuGroupId, menuProducts);
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct createMenuProduct() {
        return createMenuProduct(SEQ, MENU_ID, PRODUCT_ID, QUANTITY);
    }

    public static List<MenuProduct> createMenuProducts() {
        return Collections.singletonList(createMenuProduct());
    }
}
