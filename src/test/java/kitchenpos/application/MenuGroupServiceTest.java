package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹 생성 기능 테스트")
    @Test
    void create() {
        String name = "추천메뉴";
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 목록 조희 기능 테스트")
    @Test
    void list() {
        menuGroupService.create(new MenuGroupRequest("name1"));
        menuGroupService.create(new MenuGroupRequest("name2"));

        assertThat(menuGroupService.list()).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        menuGroupDao.deleteAll();
    }
}
