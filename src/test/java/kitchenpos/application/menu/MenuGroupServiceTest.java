package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.dto.menu.request.MenuGroupCreateRequest;
import kitchenpos.dto.menu.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("메뉴 그룹");

        MenuGroupResponse response = menuGroupService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName())
        );
    }

    @Test
    void 메뉴_그룹의_목록을_조회할_수_있다() {
        Long menuGroup1Id = menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹1"))
                .getId();
        Long menuGroup2Id = menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹2"))
                .getId();

        List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(2)
                .extracting("id")
                .containsExactly(menuGroup1Id, menuGroup2Id);
    }
}
