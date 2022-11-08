package kitchenpos.support;

import kitchenpos.menu.application.dto.MenuGroupRequestDto;

public class MenuGroupFixture {

    public static MenuGroupRequestDto 메뉴_그룹 = 메뉴_그룹_생성("메뉴_그룹1");

    public static MenuGroupRequestDto 메뉴_그룹_생성(final String name) {
        return new MenuGroupRequestDto(name);
    }
}
