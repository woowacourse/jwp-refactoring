package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Transactional
@Sql("classpath:delete.sql")
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void create() {
        MenuGroup menuGroup = createMenuGroup("menuGroup");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 불러올 수 있어야 한다.")
    void list() {
        MenuGroup menuGroup1 = createMenuGroup("menuGroup1");
        MenuGroup menuGroup2 = createMenuGroup("menuGroup2");
        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        List<MenuGroup> expectedMenuGroups = menuGroupService.list();
        assertThat(expectedMenuGroups.size()).isEqualTo(2);
    }
}
