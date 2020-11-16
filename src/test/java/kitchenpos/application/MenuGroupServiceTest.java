package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static kitchenpos.fixture.FixtureFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroup menuGroupRequest = createMenuGroup(null, "히든 메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroupRequest);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupRequest.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        MenuGroup menuGroupRequest = createMenuGroup(null, "히든 메뉴");
        menuGroupService.create(menuGroupRequest);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(1);
    }
}