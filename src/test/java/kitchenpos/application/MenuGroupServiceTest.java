package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        MenuGroup menuGroup = newMenuGroup();

        MenuGroup create = menuGroupService.create(menuGroup);

        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        MenuGroup menuGroup = newMenuGroup();
        menuGroupService.create(menuGroup);
        menuGroupService.create(menuGroup);

        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).hasSize(2);
    }

    private MenuGroup newMenuGroup() {
        return MenuGroup.builder()
            .name("반반메뉴")
            .build();
    }
}