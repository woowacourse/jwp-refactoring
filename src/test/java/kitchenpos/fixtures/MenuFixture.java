package kitchenpos.fixtures;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.fixtures.MenuGroupFixture.*;
import static kitchenpos.fixtures.MenuProductFixture.*;
import static kitchenpos.fixtures.ProductFixture.*;

public class MenuFixture {

    public static Menu 첫번째메뉴() {
        Menu menu = new Menu();
        menu.setMenuGroupId(디저트().getId());
        menu.setName("치즈폭탄");
        menu.setPrice(new BigDecimal(53500));
        menu.setMenuProducts(Arrays.asList(치즈폭탄의치즈볼(menu, 쫀득쫀득치즈볼()),
                치즈폭탄의아메리카노(menu, 아메리카노())));
        return menu;
    }

    public static Menu 치즈폭탄() {
        Menu menu = 첫번째메뉴();
        menu.setId(1L);
        return menu;
    }

    public static Menu 무많이뿌링클() {
        Menu menu = new Menu();
        menu.setId(2L);
        menu.setMenuGroupId(치킨().getId());
        menu.setName("무많이뿌링클");
        menu.setPrice(new BigDecimal(21500));
        menu.setMenuProducts(Arrays.asList(무많이뿌링클의뿌링클(menu, 맛있는뿌링클()),
                무많이뿌링클의콜라(menu, 시원한콜라()), 무많이뿌링클의치킨무(menu, 치킨무())));
        return menu;
    }

    public static Menu 둘이서알리오갈리오() {
        Menu menu = new Menu();
        menu.setId(3L);
        menu.setMenuGroupId(양식().getId());
        menu.setName("둘이서알리오갈리오");
        menu.setMenuProducts(Arrays.asList(둘이서알리오갈리오의알리오갈리오(menu, 알리오갈리오()),
                둘이서알리오갈리오의콜라(menu, 시원한콜라())));
        return menu;
    }

    public static Menu 아메리카노한잔() {
        Menu menu = new Menu();
        menu.setId(4L);
        menu.setMenuGroupId(디저트().getId());
        menu.setName("아메리카노한잔");
        menu.setMenuProducts(Collections.singletonList(아메리카노한잔의아메리카노(menu, 아메리카노())));
        return menu;
    }
}
