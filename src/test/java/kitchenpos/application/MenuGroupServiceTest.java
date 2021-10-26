package kitchenpos.application;

import kitchenpos.annotation.IntegrationTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("MenuGroup을 등록할 수 있다.")
    public void enrollMenuGroup() {
        //given & when
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴그룹");

        //then
        assertDoesNotThrow(() -> menuGroupService.create(menuGroup));
    }

    @Test
    @DisplayName("존재하는 MenuGroup 조회를 할 수 있다.")
    public void findAll() {
        //given & when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).hasSize(4);
    }
}