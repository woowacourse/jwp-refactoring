package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {
        @DisplayName("메뉴 그룹을 생성한다.")
        @Test
        void Should_CreateMenuGroup() {
            // given
            MenuGroup menuGroup = new MenuGroup("분식");

            // when
            MenuGroup actual = menuGroupService.create(menuGroup);

            // then
            assertThat(actual.getName()).isEqualTo(menuGroup.getName());
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {
        @DisplayName("생성된 메뉴 그룹 목록을 조회한다.")
        @Test
        void Should_ReturnMenuGroupList() {
            // given
            MenuGroup menuGroup1 = new MenuGroup("분식");
            MenuGroup menuGroup2 = new MenuGroup("한식");
            MenuGroup menuGroup3 = new MenuGroup("중식");

            menuGroupService.create(menuGroup1);
            menuGroupService.create(menuGroup2);
            menuGroupService.create(menuGroup3);

            // when
            List<MenuGroup> actual = menuGroupService.list();

            // then
            assertThat(actual).hasSize(3);
        }
    }


}
