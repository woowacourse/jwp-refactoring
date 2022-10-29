package kitchenpos.support;

import java.util.List;
import java.util.Map;
import kitchenpos.domain.MenuGroup;

public abstract class MenuGroupFixture {

    public static final MenuGroup 메뉴_그룹 = new MenuGroup("메뉴 그룹");
    public static final MenuGroup 단짜_두_마리_메뉴 = new MenuGroup("단짜 두 마리 메뉴");
    public static final MenuGroup 간장_양념_세_마리_메뉴 = new MenuGroup("간장 양념 세 마리 메뉴");

    public static MenuGroup menuGroup(Long id) {
        return new MenuGroup(id, "메뉴 그룹");
    }
}
