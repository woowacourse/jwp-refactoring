package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IsolatedTest;
import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest extends IsolatedTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void createMenuGroupByValidInput() {
        MenuGroup menuGroupRequest = createMenuGroup(null, "한마리 치킨");

        MenuGroup menuGroup = menuGroupService.create(menuGroupRequest);

        assertAll(
            () -> assertThat(menuGroup.getId()).isNotNull(),
            () -> assertThat(menuGroup.getName()).isEqualTo(menuGroupRequest.getName())
        );
    }

    @Test
    void findAll() {
        MenuGroup menuGroupRequest = createMenuGroup(null, "한마리 치킨");

        MenuGroup menuGroup = menuGroupService.create(menuGroupRequest);

        List<MenuGroup> groups = menuGroupService.list();

        assertThat(groups).size().isEqualTo(1);
    }
}