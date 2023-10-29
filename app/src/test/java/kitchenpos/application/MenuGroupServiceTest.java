package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.menugroup.dto.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.response.MenuGroupResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_등록한다() {
        final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("메뉴그룹");
        final MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupCreateRequest);

        assertThat(menuGroupResponse)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroupCreateRequest);
    }

    @Test
    void 메뉴_그룹의_목록을_조회한다() {
        final List<MenuGroupResponse> expected = menuGroupService.list();
        for (int i = 0; i < 3; i++) {
            final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("메뉴그룹" + i);
            expected.add(menuGroupService.create(menuGroup));
        }

        final List<MenuGroupResponse> result = menuGroupService.list();

        assertThat(result).hasSize(expected.size());
    }
}
