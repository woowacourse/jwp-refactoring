package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuFixture {

    private static final Long ID = 1L;
    private static final String NAME = "이달의치킨";
    private static final BigDecimal PRICE = BigDecimal.valueOf(20_000);
    private static final Long MENU_GROUP_ID = 1L;

    private static final Long SEQ = 1L;
    private static final Long MENU_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    public static Menu create() {
        return create(ID, NAME, PRICE, MENU_GROUP_ID, menuProducts());
    }

    public static Menu create(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static MenuProduct menuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(SEQ);
        menuProduct.setMenuId(MENU_ID);
        menuProduct.setProductId(PRODUCT_ID);
        menuProduct.setQuantity(QUANTITY);

        return menuProduct;
    }

    public static List<MenuProduct> menuProducts() {
        return Collections.singletonList(menuProduct());
    }
}
