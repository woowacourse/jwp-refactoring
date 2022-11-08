package kitchenpos.menugroup.application;

import static kitchenpos.common.fixture.Fixture.두마리메뉴_생성요청;
import static kitchenpos.common.fixture.Fixture.한마리메뉴_생성요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.menugroup.application.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class MenuGroup을_입력받는_경우 extends ServiceTest {

            private static final String 반반메뉴 = "반반메뉴";

            private final MenuGroupCreateRequest request = new MenuGroupCreateRequest(반반메뉴);

            @Test
            void 저장하고_값을_반환한다() {
                final MenuGroup actual = menuGroupService.create(request);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getName()).isEqualTo(반반메뉴)
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출하는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 2;

            @Test
            void MenuGroup의_목록을_반환한다() {
                menuGroupService.create(한마리메뉴_생성요청);
                menuGroupService.create(두마리메뉴_생성요청);

                final List<MenuGroup> actual = menuGroupService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }
}
