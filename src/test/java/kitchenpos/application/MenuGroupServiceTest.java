package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.request.MenuGroupCreateRequest;
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
    class create_메서드는 {

        @Nested
        class MenuGroup을_입력받는_경우 {

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
        class 호출하는_경우 {

            private static final int EXPECT_SIZE = 4;

            @Test
            void MenuGroup의_목록을_반환한다() {
                final List<MenuGroup> actual = menuGroupService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }
}
