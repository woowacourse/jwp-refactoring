package kitchenpos.application;

import kitchenpos.application.test.IntegrateServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static kitchenpos.domain.MenuGroupFixture.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuGroupServiceTest extends IntegrateServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_생성 {
        @Test
        void 메뉴를_생성한다() {
            // given
            MenuGroup 인기_메뉴 = 인기_메뉴_생성();

            // when, then
            assertDoesNotThrow(() -> menuGroupService.create(인기_메뉴));
        }
    }

}