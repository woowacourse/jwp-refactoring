package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

public class MenuFixture {
    private static final Long ID = 1L;
    private static final String NAME = "MENU_NAME";
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final Long MENU_GROUP_ID = 1L;

    private static final Long SEQ = 1L;
    private static final Long MENU_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    public static MenuRequest createMenuRequest() {
        return new MenuRequest(NAME, PRICE, MENU_GROUP_ID, createMenuProducts());
    }

    public static MenuRequest createMenuRequest(Long menuGroupId, List<MenuProduct> menuProducts) {
        return new MenuRequest(NAME, PRICE, menuGroupId, menuProducts);
    }

    public static MenuRequest createMenuRequest(BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new MenuRequest(NAME, price, menuGroupId, menuProducts);
    }

    public static MenuResponse createMenuResponse() {
        return new MenuResponse(ID, NAME, PRICE, MENU_GROUP_ID, createMenuProducts());
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
