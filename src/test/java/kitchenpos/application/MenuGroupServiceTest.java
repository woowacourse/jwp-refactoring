package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuGroupFixture.getMenuGroup;
import static kitchenpos.support.fixture.dto.MenuGroupCreateRequestFixture.menuGroupCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.menu.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_등록한다() {
        //given
        final MenuGroupCreateRequest menuGroup = menuGroupCreateRequest("menuGroup1");

        //when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(menuGroupRepository.findById(savedMenuGroup.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        //given
        final MenuGroup menuGroup1 = menuGroupRepository.save(getMenuGroup("menuGroup1"));
        final MenuGroup menuGroup2 = menuGroupRepository.save(getMenuGroup("menuGroup2"));

        //when
        final List<MenuGroup> result = menuGroupService.list();

        //then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(menuGroup1, menuGroup2));
    }
}
