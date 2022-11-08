package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("한판 메뉴");
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroupCreateRequest);

        assertThat(menuGroupRepository.existsById(savedMenuGroup.getId())).isTrue();
    }

    @Test
    void 메뉴그룹_리스트를_반환한다() {
        int beforeSize = menuGroupService.list().size();
        menuGroupRepository.save(new MenuGroup("한판 메뉴"));

        assertThat(menuGroupService.list().size()).isEqualTo(beforeSize + 1);
    }
}
