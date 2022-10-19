package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class create_메소드는 {

        @Nested
        class 메뉴그룹을_입력받는_경우 {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");

            @Test
            void 저장한다() {
                MenuGroup actual = menuGroupService.create(menuGroup);

                assertThat(actual.getId()).isNotNull();
            }
        }
    }
}
