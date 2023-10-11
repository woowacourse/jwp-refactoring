package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void MenuGroup_을_생성할_수_있다() {
        //given
        final MenuGroup menuGroup = new MenuGroup("치킨");

        //when
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @Test
    void MenuGroup_을_조회할_수_있다() {
        //when
        final var result = menuGroupService.list();

        //then
        assertThat(result).hasSize(4);
    }
}
