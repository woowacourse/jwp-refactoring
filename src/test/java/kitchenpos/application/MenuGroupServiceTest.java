package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.SpringServiceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    @Nested
    class create_메소드는 {

        @Nested
        class 메뉴그룹을_입력받는_경우 extends SpringServiceTest {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");

            @Test
            void 저장한다() {
                MenuGroup actual = menuGroupService.create(menuGroup);

                assertThat(actual.getId()).isNotNull();
            }
        }
    }

    @Nested
    class list_메소드는 {

        @Nested
        class 요청이_들어오는_경우 extends SpringServiceTest {

            @Test
            void 메뉴그룹목록을_반환한다() {
                List<MenuGroup> actual = menuGroupService.list();

                assertThat(actual).hasSize(4);
            }
        }
    }
}
