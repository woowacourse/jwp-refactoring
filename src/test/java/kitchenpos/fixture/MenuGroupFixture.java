package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

import java.util.Arrays;
import java.util.List;

public class MenuGroupFixture {

    public MenuGroup 메뉴그룹_생성(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(null);
        menuGroup.setName(name);
        return menuGroup;
    }

    public MenuGroup 메뉴그룹_생성(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public List<MenuGroup> 메뉴그룹_리스트_생성(MenuGroup... menuGroups) {
        return Arrays.asList(menuGroups);
    }
}
