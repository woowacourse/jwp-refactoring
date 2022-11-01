package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.request.MenuGroupRequest;

public class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        MenuGroupRequest request = createRequest("세마리세트");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        MenuGroup savedMenuGroup = menuGroupService.create(createRequest("세마리세트"));

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).contains(savedMenuGroup);
    }

    private MenuGroupRequest createRequest(final String name) {
        return new MenuGroupRequest(null, name);
    }
}
