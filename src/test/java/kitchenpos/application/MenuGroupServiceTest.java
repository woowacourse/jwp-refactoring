package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import kitchenpos.menu.dto.request.CreateMenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.util.ObjectCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final int newMenuGroupId = menuGroupService.list().size() + 1;
        final CreateMenuGroupRequest request = ObjectCreator.getObject(CreateMenuGroupRequest.class, "test");
        // when
        final MenuGroupResponse actual = menuGroupService.create(request);

        // then
        assertThat(actual.getId()).isEqualTo(newMenuGroupId);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void list() {
        // given & when
        final List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}
