package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup firstMenuGroup = new MenuGroup("1번 메뉴 그룹");

        MenuGroup menuGroup = menuGroupService.create(firstMenuGroup);

        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    @Test
    void list() {
        menuGroupService.create(new MenuGroup("1번 메뉴 그룹"));
        menuGroupService.create(new MenuGroup("2번 메뉴 그룹"));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(2);
    }
}
