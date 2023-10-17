package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void MenuGroup_을_생성할_수_있다() {
        //when
        final Long menuGroupId = menuGroupService.create("치킨");

        //then
        assertThat(menuGroupId).isNotNull();
    }

    @Test
    void MenuGroup_을_조회할_수_있다() {
        //given
        final MenuGroup chickenGroup = new MenuGroup("치킨");
        final MenuGroup pizzaGroup = new MenuGroup("피자");
        menuGroupRepository.save(chickenGroup);
        menuGroupRepository.save(pizzaGroup);

        //when
        final var result = menuGroupService.list();

        //then
        assertThat(result).hasSize(2);
    }
}
