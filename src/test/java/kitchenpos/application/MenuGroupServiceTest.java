package kitchenpos.application;

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
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.builder()
            .name("반반메뉴")
            .build();
    }

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        menuGroupService.create(menuGroup);
        menuGroupService.create(menuGroup);

        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).hasSize(2);
    }
}