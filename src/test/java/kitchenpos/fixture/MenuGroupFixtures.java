package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static MenuGroup TEST_GROUP() {
        return new MenuGroup("테스트 그룹");
    }
}
