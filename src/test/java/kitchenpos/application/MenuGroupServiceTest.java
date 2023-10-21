package kitchenpos.application;

import java.util.List;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create_menuGroup() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("메뉴 그룹");

        // when
        final MenuGroupResponse result = menuGroupService.create(request);

        // then
        assertThat(result.getName()).isEqualTo(request.getName());
    }

    @DisplayName("전체 메뉴 그룹을 가져온다.")
    @Test
    void find_all_menuGroup() {
        // given
        final MenuGroupRequest request1 = new MenuGroupRequest("메뉴 그룹1");
        final MenuGroupRequest request2 = new MenuGroupRequest("메뉴 그룹2");
        menuGroupService.create(request1);
        menuGroupService.create(request2);

        // when
        final List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
