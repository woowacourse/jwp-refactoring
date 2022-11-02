package kitchenpos.menu.application;

import static kitchenpos.DtoFixture.getMenuGroupCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        final MenuGroupCreateRequest request = getMenuGroupCreateRequest();

        final MenuGroup savedMenuGroup = 메뉴_그룹_등록(request);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(request.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        메뉴_그룹_등록(getMenuGroupCreateRequest());

        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(1);
    }
}
