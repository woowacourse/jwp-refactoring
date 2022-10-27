package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {
    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        final MenuGroup menuGroup = saveAndGetMenuGroup(1L);

        final MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual.getName()).isEqualTo("애기메뉴목록");
    }

    @Test
    @DisplayName("메뉴 그룹 전체를 조회한다")
    void list() {
        saveAndGetMenuGroup(1L);

        final List<MenuGroup> actual = menuGroupService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("id")
                        .containsExactly(1L)
        );
    }
}
