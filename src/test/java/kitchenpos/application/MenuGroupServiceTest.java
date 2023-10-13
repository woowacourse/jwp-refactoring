package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.두마리메뉴;
import static kitchenpos.fixture.MenuFixture.신메뉴;
import static kitchenpos.fixture.MenuFixture.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Test
    @DisplayName("menuGroup을 생성하는 기능")
    void create() {
        //given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test");

        //when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(menuGroupService.list())
            .usingRecursiveFieldByFieldElementComparator()
            .contains(savedMenuGroup);
    }

    @Test
    @DisplayName("menuGroup 전체를 조회하는 기능")
    void list() {
        final List<MenuGroup> menuGroup = List.of(두마리메뉴(), 신메뉴(), 한마리메뉴());

        menuGroup.forEach(menuGroupService::create);

        assertThat(menuGroupService.list())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactlyInAnyOrderElementsOf(menuGroup);
    }
}
