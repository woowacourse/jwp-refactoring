package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import kitchenpos.common.IntegrationTest;
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

        // expect
        assertThatNoException().isThrownBy(() -> menuGroupService.create(menuGroupCreateRequest));
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
