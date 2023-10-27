package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import kitchenpos.supports.ServiceTestContext;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTestContext {

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        CreateMenuGroupRequest request = new CreateMenuGroupRequest("menuGroup");

        // when
        MenuGroupResponse response = menuGroupService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 모든_메뉴_그룹을_조회할_수_있다() {
        // given
        CreateMenuGroupRequest request = new CreateMenuGroupRequest("menuGroup");
        menuGroupService.create(request);

        // when
        List<MenuGroupResponse> response = menuGroupService.findAll();

        // then
        assertThat(response).hasSize(1);
    }
}
