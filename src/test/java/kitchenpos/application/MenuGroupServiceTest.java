package kitchenpos.application;

import kitchenpos.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("새로운 메뉴 그룹 생성")
    @Test
    void createMenuGroupTest() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("반마리메뉴");

        MenuGroupResponse menuGroupResponse = this.menuGroupService.createMenuGroup(menuGroupCreateRequest);

        assertAll(
                () -> assertThat(menuGroupResponse).isNotNull(),
                () -> assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupCreateRequest.getName())
        );
    }

    @DisplayName("존재하는 모든 메뉴 그룹을 조회")
    @Test
    void listMenuGroupTest() {
        MenuGroupCreateRequest menuGroupCreateRequest1 = new MenuGroupCreateRequest("두마리메뉴");
        MenuGroupCreateRequest menuGroupCreateRequest2 = new MenuGroupCreateRequest("세마리메뉴");
        List<MenuGroupCreateRequest> menuGroupCreateRequests = Arrays.asList(menuGroupCreateRequest1,
                                                                             menuGroupCreateRequest2);
        menuGroupCreateRequests.forEach(menuGroupCreateRequest -> this.menuGroupService.createMenuGroup(menuGroupCreateRequest));

        List<MenuGroupResponse> menuGroupResponses = this.menuGroupService.listAllMenuGroups();

        assertThat(menuGroupResponses).hasSize(menuGroupCreateRequests.size());
    }
}