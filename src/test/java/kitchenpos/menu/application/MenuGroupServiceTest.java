package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dto.MenuGroupCreateResponse;
import kitchenpos.menu.dto.MenuGroupFindResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {
    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        final MenuGroup menuGroup = saveAndGetMenuGroup(1L);

        final MenuGroupCreateResponse actual =
                menuGroupService.create(new MenuGroupCreateRequest(menuGroup.getName()));

        assertThat(actual.getName()).isEqualTo("애기메뉴목록");
    }

    @Test
    @DisplayName("메뉴 그룹 전체를 조회한다")
    void list() {
        saveAndGetMenuGroup(1L);

        final List<MenuGroupFindResponse> actual = menuGroupService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("id")
                        .containsExactly(1L)
        );
    }
}
