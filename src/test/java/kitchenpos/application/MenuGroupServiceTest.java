package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroupService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {
            private final String name = "햄버거 세트";
            private final MenuGroup menuGroup = new MenuGroup(name);

            @Test
            void 메뉴_그룹을_추가한다() {
                MenuGroup actual = menuGroupService.create(menuGroup);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isEqualTo(name);
                });
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            @Test
            void 메뉴_그룹_목록을_반환한다() {
                List<MenuGroup> menuGroups = menuGroupService.list();

                assertThat(menuGroups).isNotEmpty();
            }
        }
    }
}
