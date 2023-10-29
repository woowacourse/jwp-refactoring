package kitchenpos.fixture;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹_신메뉴() {
        return new MenuGroup("신메뉴");
    }

    public static MenuGroup 메뉴그룹_존재X() {
        return new MenuGroup(999999L, "INVALID");
    }

    public static MenuGroupCreateRequest 메뉴그룹요청_생성(final MenuGroup menuGroup) {
        return new MenuGroupCreateRequest(menuGroup.getName());
    }
}
