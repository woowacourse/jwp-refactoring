package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MenuFixture {
    public static final Menu 후라이드치킨;
    public static final Menu 후라이드치킨_NO_KEY;
    public static final Menu 양념치킨;
    public static final Menu 반반치킨;
    public static final Menu 통구이;
    public static final Menu 간장치킨;
    public static final Menu 순살치킨;
    public static final Menu 후라이드_후라이드;

    static {
        후라이드치킨 = newInstance(1L, "후라이드치킨", new BigDecimal(16000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.후라이드치킨_후라이드치킨));
        후라이드치킨_NO_KEY = newInstance(1L, "후라이드치킨", new BigDecimal(16000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY));
        양념치킨 = newInstance(2L, "양념치킨", new BigDecimal(16000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.양념치킨_양념치킨));
        반반치킨 = newInstance(3L, "반반치킨", new BigDecimal(16000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.반반치킨_반반치킨));
        통구이 = newInstance(4L, "통구이", new BigDecimal(17000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.통구이_통구이));
        간장치킨 = newInstance(5L, "간장치킨", new BigDecimal(17000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.간장치킨_간장치킨));
        순살치킨 = newInstance(6L, "순살치킨", new BigDecimal(17000), MenuGroupFixture.한마리메뉴.getId(), Collections.singletonList(MenuProductFixture.순살치킨_순살치킨));
        후라이드_후라이드 = newInstance(7L, "후라이드+후라이드", new BigDecimal(19000), MenuGroupFixture.두마리메뉴.getId(), Collections.singletonList(MenuProductFixture.후라이드후라이드_후라이드치킨));
    }

    public static List<Menu> menus() {
        return Arrays.asList(후라이드치킨, 양념치킨, 반반치킨, 통구이, 간장치킨, 순살치킨, 후라이드_후라이드);
    }

    public static List<String> menusName() {
        return menus().stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
    }

    private static Menu newInstance(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        final Menu menuGroup = new Menu();
        menuGroup.setId(id);
        menuGroup.setName(name);
        menuGroup.setPrice(price);
        menuGroup.setMenuGroupId(menuGroupId);
        menuGroup.setMenuProducts(menuProducts);
        return menuGroup;
    }
}
