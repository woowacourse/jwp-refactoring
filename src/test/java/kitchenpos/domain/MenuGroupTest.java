package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        MenuGroup menuGroup = new MenuGroup("고기");
    }
}
