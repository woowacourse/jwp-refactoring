package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.두마리메뉴;
import static kitchenpos.fixture.MenuFixture.순살파닭두마리메뉴;
import static kitchenpos.fixture.MenuFixture.신메뉴;
import static kitchenpos.fixture.MenuFixture.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

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
        assertThat(menuGroupService.list())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .containsExactlyInAnyOrder(
                두마리메뉴(),
                신메뉴(),
                한마리메뉴(),
                순살파닭두마리메뉴()
            );
    }
}
