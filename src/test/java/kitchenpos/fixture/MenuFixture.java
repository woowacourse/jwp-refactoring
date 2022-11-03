package kitchenpos.fixture;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_2;

import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.Price;

public class MenuFixture {

    public static final Menu MENU_1 = new Menu(1L, "후라이드치킨", MENU_GROUP_2, new Price(16000));
    public static final Menu MENU_2 = new Menu(2L, "양념치킨", MENU_GROUP_2, new Price(16000));
    public static final Menu MENU_3 = new Menu(3L, "반반치킨", MENU_GROUP_2, new Price(16000));
    public static final Menu MENU_4 = new Menu(4L, "통구이", MENU_GROUP_2, new Price(16000));
    public static final Menu MENU_5 = new Menu(5L, "간장치킨", MENU_GROUP_2, new Price(17000));
    public static final Menu MENU_6 = new Menu(6L, "순살치킨", MENU_GROUP_2, new Price(17000));
}
