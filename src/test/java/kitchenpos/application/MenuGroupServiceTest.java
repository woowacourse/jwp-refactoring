package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static kitchenpos.DomainFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class MenuGroupServiceTest {
    private static final String DELETE_MENU_GROUP = "delete from menu_group where id in (:ids)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;
    private List<Long> menuGroupIds;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
        menuGroupIds = new ArrayList<>();
    }

    @DisplayName("메뉴 그룹 저장")
    @Test
    void createTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("저장된 모든 메뉴 그룹 반환")
    @Test
    void listTest() {
        MenuGroup singleMenuGroup = createMenuGroup("한마리메뉴");
        MenuGroup doubleMenuGroup = createMenuGroup("두마리메뉴");
        menuGroupDao.save(singleMenuGroup);
        menuGroupDao.save(doubleMenuGroup);

        List<MenuGroup> allMenuGroups = menuGroupService.list();
        allMenuGroups.forEach(menuGroup -> menuGroupIds.add(menuGroup.getId()));

        assertThat(allMenuGroups).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        Map<String, Object> params = Collections.singletonMap("ids", menuGroupIds);
        namedParameterJdbcTemplate.update(DELETE_MENU_GROUP, params);
    }
}