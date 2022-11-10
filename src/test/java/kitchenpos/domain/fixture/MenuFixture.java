package kitchenpos.domain.fixture;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private MenuFixture() {
    }

    public static Menu 후라이드_치킨_세트의_가격과_메뉴_상품_리스트는(final Long menuGroupId, final BigDecimal price, final List<MenuProduct> menuProducts) {
        return 메뉴()
            .메뉴_그룹_아이디(menuGroupId)
            .이름("후라이드 치킨 세트")
            .가격(price)
            .메뉴_상품_리스트(menuProducts)
            .build();
    }

    private static MenuFixture 메뉴() {
        return new MenuFixture();
    }

    private MenuFixture 메뉴_그룹_아이디(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    private MenuFixture 이름(final String name) {
        this.name = name;
        return this;
    }

    private MenuFixture 가격(final BigDecimal price) {
        this.price = price;
        return this;
    }

    private MenuFixture 메뉴_상품_리스트(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    private Menu build() {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}
