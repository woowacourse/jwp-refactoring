package kitchenpos.fixture;

import static kitchenpos.fixture.MenuProductFixture.createMenuProducts;

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

    public static Menu createMenu() {
        Menu menu = new Menu();
        menu.setId(ID);
        menu.setName(NAME);
        menu.setPrice(PRICE);
        menu.setMenuGroupId(MENU_GROUP_ID);
        menu.setMenuProducts(createMenuProducts());
        return menu;
    }
}

class MenuProductFixture {
    private static final Long SEQ = 1L;
    private static final Long MENU_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    public static MenuProduct createMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(SEQ);
        menuProduct.setMenuId(MENU_ID);
        menuProduct.setProductId(PRODUCT_ID);
        menuProduct.setQuantity(QUANTITY);
        return menuProduct;
    }

    public static List<MenuProduct> createMenuProducts() {
        return Collections.singletonList(createMenuProduct());
    }
}
