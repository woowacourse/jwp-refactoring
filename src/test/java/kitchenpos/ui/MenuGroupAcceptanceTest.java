package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroup 인수 테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        MenuGroupRequest request = new MenuGroupRequest("menu-group");
        MenuGroupResponse response = makeResponse("/api/menu-groups/", TestMethod.POST, request)
            .as(MenuGroupResponse.class);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getName()).isEqualTo(request.getName())
        );
    }

    @Test
    void list() {
        MenuGroupRequest request = new MenuGroupRequest("menu-group");
        makeResponse("/api/menu-groups/", TestMethod.POST, request)
            .as(MenuGroupResponse.class);
        makeResponse("/api/menu-groups/", TestMethod.POST, request)
            .as(MenuGroupResponse.class);

        List<MenuGroupResponse> responses = makeResponse("/api/menu-groups/", TestMethod.GET).jsonPath()
            .getList(".", MenuGroupResponse.class);

        assertAll(
            () -> assertThat(responses.size()).isEqualTo(2),
            () -> assertThat(responses.stream()
                .map(MenuGroupResponse::getName).collect(Collectors.toList()))
                .containsExactly("menu-group", "menu-group")
        );
    }
}