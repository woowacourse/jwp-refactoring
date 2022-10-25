package kitchenpos.domain.fixture;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private MenuFixture() {
    }

    public static MenuFixture 후라이드_치킨_세트() {
        final MenuFixture menuFixture = new MenuFixture();
        menuFixture.name = "후라이드";
        return menuFixture;
    }

    public MenuFixture 메뉴_그룹_아이디(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public MenuFixture 가격(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuFixture 메뉴_상품_리스트(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
