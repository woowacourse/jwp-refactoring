package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.common.annotation.IntegrationTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("신메뉴");

        // when
        MenuGroup menuGroup = menuGroupService.create(menuGroupCreateRequest);

        // then
        assertThat(menuGroup.name()).isEqualTo("신메뉴");
    }

    @Test
    void 저장된_모든_메뉴_그룹을_조회한다() {
        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isNotNull();
        assertThat(menuGroups.size()).isGreaterThanOrEqualTo(0);
    }
}
