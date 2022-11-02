package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.request.MenuGroupCreateRequest;
import kitchenpos.menu.application.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("name");

        MenuGroupResponse response = menuGroupService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void list() {
        List<MenuGroupResponse> response = menuGroupService.list();

        assertThat(response.size()).isEqualTo(4);
    }
}
