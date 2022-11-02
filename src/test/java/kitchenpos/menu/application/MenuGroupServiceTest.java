package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.application.response.MenuGroupResponse;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.application.request.MenuGroupRequest;

public class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        MenuGroupRequest request = createRequest("세마리세트");

        // when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        MenuGroupResponse savedMenuGroup = menuGroupService.create(createRequest("세마리세트"));

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertMenuGroupResponse(result.get(0), savedMenuGroup);
    }

    private MenuGroupRequest createRequest(final String name) {
        return new MenuGroupRequest(null, name);
    }

    private void assertMenuGroupResponse(final MenuGroupResponse actual, final MenuGroupResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }
}
