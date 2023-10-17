package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateDto;

public class MenuGroupFixture {

    public static final MenuGroup 메뉴그룹_두마리메뉴 = 메뉴그룹("두마리메뉴");
    public static final MenuGroup 메뉴그룹_한마리메뉴 = 메뉴그룹("한마리메뉴");
    public static final MenuGroup 메뉴그룹_신메뉴 = 메뉴그룹("신메뉴");

    public static MenuGroupCreateDto 메뉴그룹_생성_요청(String name) {
        return new MenuGroupCreateDto(name);
    }

    public static MenuGroup 메뉴그룹(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
