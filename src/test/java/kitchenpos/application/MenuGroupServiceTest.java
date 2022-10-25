package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        // given
        String menuName = "두마리메뉴";

        MenuGroup menuGroup = getMenuGroup(1L, menuName);

        given(menuGroupDao.save(any()))
                .willReturn(menuGroup);

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(menuName)
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup1 = getMenuGroup(1L, "한마리메뉴");
        MenuGroup menuGroup2 = getMenuGroup(2L, "두마리메뉴");

        given(menuGroupDao.findAll())
                .willReturn(List.of(menuGroup1, menuGroup2));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
