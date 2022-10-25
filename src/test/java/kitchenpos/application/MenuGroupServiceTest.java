package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        MenuGroup request = createMenuGroup(null, menuName);

        // when
        MenuGroup actual = menuGroupService.create(request);

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
        saveAndGetMenuGroup();

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
