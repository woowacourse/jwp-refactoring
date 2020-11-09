package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MenuGroupServiceTest {

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
        assertThat(groups.get(0).getName()).isEqualTo("한마리 치킨");
    }
}