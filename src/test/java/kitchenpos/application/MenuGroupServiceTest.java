package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void createMenuGroup() {
        final String newMenuGroupName = "베스트메뉴";
        final MenuGroup menuGroup = new MenuGroup(newMenuGroupName);

        final MenuGroup persistedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(persistedMenuGroup.getName()).isEqualTo(newMenuGroupName);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 가져 온다.")
    void getMenuGroupList() {
        final List<MenuGroup> list = menuGroupService.list();

        assertThat(list).hasSize(4);
    }
}
