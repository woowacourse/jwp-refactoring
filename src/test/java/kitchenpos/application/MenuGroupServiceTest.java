package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
    }

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        MenuGroup menuGroup = createMenuGroup("반반메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        MenuGroup menuGroup1 = createMenuGroup("반반메뉴");
        MenuGroup menuGroup2 = createMenuGroup("강정메뉴");
        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).hasSize(2);
    }
}