package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.dto.menugroup.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {
    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        // given
        final MenuGroupCreateRequest request = new MenuGroupCreateRequest("menuGroupName");

        // when
        final Long id = menuGroupService.create(request);

        // then
        assertThat(id).isPositive();
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회")
    void getMenuGroups() {
        // given
        final MenuGroupCreateRequest request = new MenuGroupCreateRequest("menuGroupName");
        menuGroupService.create(request);

        // when
        final List<MenuGroupResponse> responses = menuGroupService.list();

        // then
        assertThat(responses).isNotEmpty();
    }
}
