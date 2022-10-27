package kitchenpos.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuFixture {

    후라이드_양념치킨_두마리세트("후라이드, 양념치킨 두마리 세트", new BigDecimal(30_000));

    private final String name;
    private final BigDecimal price;

    MenuFixture(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Menu toMenu(Long menuGroupId, Long... productIds) {
        Menu menu = new Menu(name, price, menuGroupId);
        addMenuProducts(menu, productIds);
        return menu;
    }

    public Menu toMenu(BigDecimal price, Long menuGroupId, Long... productIds) {
        Menu menu = new Menu(name, price, menuGroupId);
        addMenuProducts(menu, productIds);
        return menu;
    }

    private static void addMenuProducts(Menu menu, Long[] productIds) {
        for (Long productId : productIds) {
            menu.addMenuProduct(new MenuProduct(productId, 1));
        }
    }
}
