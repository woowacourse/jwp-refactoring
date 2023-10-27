package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixtures {

    public static MenuGroup TEST_GROUP() {
        return new MenuGroup("테스트 그룹");
    }
}
