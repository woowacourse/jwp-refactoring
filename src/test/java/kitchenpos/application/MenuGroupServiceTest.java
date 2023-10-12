package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {

        @Test
        void 정상_요청() {
            // given
            MenuGroup menuGroup = createMenuGroup("한식");

            // when
            MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            // then
            assertThat(savedMenuGroup)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menuGroup);
        }
    }

    @Nested
    class 메뉴_그룹_전체_조회 {

        @Test
        void 정상_요청() {
            // given
            MenuGroup menuGroup = createMenuGroup("중식");
            MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            // when
            List<MenuGroup> menuGroups = menuGroupService.list();

            // then
            assertThat(menuGroups)
                    .extracting(MenuGroup::getName)
                    .contains(savedMenuGroup.getName());
        }
    }

    private MenuGroup createMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
