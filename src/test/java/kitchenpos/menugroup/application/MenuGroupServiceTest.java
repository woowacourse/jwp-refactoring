package kitchenpos.menugroup.application;

import static kitchenpos.fixture.MenuGroupFixture.CHICKEN_SET;
import static kitchenpos.fixture.MenuGroupFixture.CHICKEN_SET_NON_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create_메서드는_메뉴_그룹을_생성한다() {
        // when
        final MenuGroup response = menuGroupService.create(new MenuGroupRequest("치킨세트"));

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(CHICKEN_SET);
    }

    @Test
    void list_메서드는_모든_메뉴_그룹을_조회한다() {
        // given
        menuGroupRepository.save(CHICKEN_SET_NON_ID);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups)
                .usingRecursiveComparison()
                .isEqualTo(List.of(CHICKEN_SET));
    }
}
