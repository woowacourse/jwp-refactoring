package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.두마리메뉴;
import static kitchenpos.fixture.MenuFixture.순살파닭두마리메뉴;
import static kitchenpos.fixture.MenuFixture.신메뉴;
import static kitchenpos.fixture.MenuFixture.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Test
    @DisplayName("menuGroup을 생성하는 기능")
    void create() {
        //when
        final MenuGroup savedMenuGroup = menuGroupService.create(순살파닭두마리메뉴());

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
