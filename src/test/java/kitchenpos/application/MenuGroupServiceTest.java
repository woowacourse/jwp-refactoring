package kitchenpos.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroupService 클래스의")
class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("create 메서드는 메뉴 분류를 생성한다.")
    void create() {
        // given
        MenuGroup menuGroup = createMenuGroup("반마리치킨");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        Optional<MenuGroup> actual = menuGroupDao.findById(savedMenuGroup.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("list 메서드는 모든 메뉴 분류를 조회한다.")
    void list() {
        // when
        List<MenuGroup> list = menuGroupService.list();
        for (MenuGroup menuGroup : list) {
            System.out.println("menuGroup.getName() = " + menuGroup.getName());
        }

        // then
        assertThat(list).hasSize(0);
    }
}
