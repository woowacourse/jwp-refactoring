package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    private final MenuGroup menuGroup = new MenuGroup("한마리메뉴");

    @Nested
    class create_메서드는 {


        @Nested
        class 메뉴_그룹이_입력되면 {

            @Test
            void 해당_메뉴_그룹을_반환한다() {
                final MenuGroup actual = menuGroupService.create(menuGroup);

                assertThat(actual).isNotNull();
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 {

            @BeforeEach
            void setUp() {
                menuGroupDao.save(menuGroup);
            }

            @Test
            void 모든_메뉴_그룹을_반환한다() {
                final List<MenuGroup> actual = menuGroupService.list();

                assertThat(actual).hasSize(1);
            }
        }
    }
}
