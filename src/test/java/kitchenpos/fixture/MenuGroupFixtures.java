package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtures {

    public static MenuGroup TEST_GROUP() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트 그룹");
        return menuGroup;
    }
}
