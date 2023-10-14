package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixtures.두마리_메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성 후 조회한다.")
    @Test
    void createAndList() {
        // given
        MenuGroup menuGroup = 두마리_메뉴;

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(1);
        assertThat(menuGroups.get(0).getName()).isEqualTo(savedMenuGroup.getName());
    }
}
