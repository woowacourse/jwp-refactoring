package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends IntegrationTest {

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "두마리메뉴");

        // when
        MenuGroup extract = menuGroupService.create(menuGroup);

        // then
        assertThat(extract).isNotNull();
    }

    @Test
    void 메뉴_그룹들을_조회할_수_있다() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "두마리메뉴");
        menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> extract = menuGroupService.list();

        // then
        assertThat(extract).hasSize(1);
    }
}