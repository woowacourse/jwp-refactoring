package kitchenpos.domain.menugroup;

import kitchenpos.domain.menugroup.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        MenuGroup menuGroup = new MenuGroup("고기");
    }
}
