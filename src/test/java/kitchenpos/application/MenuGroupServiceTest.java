package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @BeforeEach
    void beforeEach() {
        menuGroupService = new MenuGroupService(new FakeMenuGroupDao());
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한바리반메뉴");

        // when
        MenuGroup newMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(newMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        // given
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("한바리반메뉴");
        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("종합 세트");
        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);
        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.size()).isEqualTo(2);
    }
}
