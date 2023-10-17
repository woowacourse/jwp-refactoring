package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup("추천메뉴");

        // when
        final MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void list() {
        // given
        final MenuGroup menuGroup1 = menuGroupService.create(new MenuGroup("Group1"));
        final MenuGroup menuGroup2 = menuGroupService.create(new MenuGroup("Group2"));

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(menuGroup1, menuGroup2);
    }
}
