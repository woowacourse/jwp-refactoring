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

    public static Menu 후라이드_치킨_세트() {
        return 메뉴()
            .이름("후라이드")
            .build();
    }

    public static Menu 후라이드_치킨_세트(final Long menuGroupId) {
        return 메뉴()
            .메뉴_그룹_아이디(menuGroupId)
            .이름("후라이드")
            .build();
    }

    public static Menu 후라이드_치킨_세트의_가격은(final Long menuGroupId, final BigDecimal price) {
        return 메뉴()
            .메뉴_그룹_아이디(menuGroupId)
            .이름("후라이드")
            .가격(price)
            .build();
    }

    public static Menu 후라이드_치킨_세트의_가격과_메뉴_상품_리스트는(final Long menuGroupId, final BigDecimal price, final List<MenuProduct> menuProducts) {
        return 메뉴()
            .메뉴_그룹_아이디(menuGroupId)
            .가격(price)
            .메뉴_상품_리스트(menuProducts)
            .build();
    }

    public static Menu 후라이드_치킨_세트의_메뉴_상품들은(final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return 메뉴()
            .메뉴_그룹_아이디(menuGroupId)
            .가격(new BigDecimal(15_000))
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
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
