package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

import static kitchenpos.MenuProductFixture.*;

public class MenuFixture {
    private static MenuGroup 추천메뉴 = new MenuGroup(1L, "추천 메뉴");
    private static MenuGroup 신메뉴 = new MenuGroup(2L, "신메뉴");

    public static Menu 후라이드_단품 = new Menu(
            1L,
            "후라이드 단품",
            BigDecimal.valueOf(17000),
            추천메뉴.getId(),
            Collections.singletonList(후라이드치킨_한마리_메뉴상품)
    );

    public static Menu 양념_단품 = new Menu(
            2L,
            "양념 단품",
            BigDecimal.valueOf(17000),
            추천메뉴.getId(),
            Collections.singletonList(양념치킨_한마리_메뉴상품)
    );

    public static Menu 양념반_후라이드반 = new Menu(
            3L,
            "양념 반 + 후라이드 반",
            BigDecimal.valueOf(30000),
            추천메뉴.getId(),
            Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품)
    );

    public static Menu 더블간장 = new Menu(
            4L,
            "더블 간장",
            BigDecimal.valueOf(29000),
            신메뉴.getId(),
            Collections.singletonList(간장치킨_두마리_메뉴상품)
    );
}
