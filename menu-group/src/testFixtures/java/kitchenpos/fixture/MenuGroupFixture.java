package kitchenpos.fixture;

import kitchenpos.menu_group.application.MenuGroupDto;
import kitchenpos.menu_group.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 두마리메뉴() {
        return new MenuGroup("두마리메뉴");
    }

    public static MenuGroup 한마리메뉴() {
        return new MenuGroup("한마리메뉴");
    }

    public static MenuGroup 순살파닭두마리메뉴() {
        return new MenuGroup("순살파닭두마리메뉴");
    }

    public static MenuGroup 신메뉴() {
        return new MenuGroup("신메뉴");
    }

    public static MenuGroupDto 두마리메뉴_DTO() {
        return new MenuGroupDto(null, "두마리메뉴");
    }

    public static MenuGroupDto 한마리메뉴_DTO() {
        return new MenuGroupDto(null, "한마리메뉴");
    }

    public static MenuGroupDto 순살파닭두마리메뉴_DTO() {
        return new MenuGroupDto(null, "순살파닭두마리메뉴");
    }

    public static MenuGroupDto 신메뉴_DTO() {
        return new MenuGroupDto(null, "신메뉴");
    }

}
