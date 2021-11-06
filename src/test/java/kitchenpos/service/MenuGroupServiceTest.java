package kitchenpos.service;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("MenuGroupService 테스트")
class MenuGroupServiceTest {

    private static final String MENU_GROUP_NAME = "MENU_GROUP_NAME";
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        //given
        MenuGroupRequest menuGroup = new MenuGroupRequest(MENU_GROUP_NAME);
        //when
        MenuGroupResponse response = menuGroupService.create(menuGroup);
        //then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 반환")
    @Test
    void list() {
        //given
        MenuGroupRequest menuGroup = new MenuGroupRequest(MENU_GROUP_NAME);
        menuGroupService.create(menuGroup);
        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.findAll();
        //then
        assertThat(menuGroups).hasSize(1);
    }
}
