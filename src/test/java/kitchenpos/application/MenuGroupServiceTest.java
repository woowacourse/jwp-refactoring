package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        // given
        final String menuName = "두마리메뉴";
        final MenuGroupCreateRequest request = createMenuGroupCreateRequest(menuName);

        // when
        final MenuGroup actual = menuGroupService.create(request);

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
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
