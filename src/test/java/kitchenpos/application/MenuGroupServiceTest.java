package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("세트메뉴");

        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("세트메뉴");
    }

    @Test
    void list() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("세트메뉴");
        MenuGroupResponse 세트메뉴 = menuGroupService.create(menuGroupRequest);

        menuGroupRequest = new MenuGroupRequest("사이드메뉴");
        MenuGroupResponse 사이드메뉴 = menuGroupService.create(menuGroupRequest);

        menuGroupRequest = new MenuGroupRequest("안주류");
        MenuGroupResponse 안주류 = menuGroupService.create(menuGroupRequest);

        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups).hasSize(3);
        assertThatMenuGroupsContainsMenuGroup(menuGroups, 세트메뉴);
        assertThatMenuGroupsContainsMenuGroup(menuGroups, 사이드메뉴);
        assertThatMenuGroupsContainsMenuGroup(menuGroups, 안주류);
    }

    void assertThatMenuGroupsContainsMenuGroup(List<MenuGroupResponse> menuGroups, MenuGroupResponse menuGroup) {
        boolean actual = menuGroups.stream()
            .anyMatch(menuGroupOfList ->
                menuGroupOfList.getId().equals(menuGroup.getId()));
        assertThat(actual).isTrue();
    }
}
