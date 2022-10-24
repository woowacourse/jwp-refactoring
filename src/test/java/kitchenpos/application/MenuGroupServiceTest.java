package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();

        // when
        final MenuGroup created = menuGroupService.create(menuGroup);

        // then
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("등록된 메뉴 그룹을 조회할 수 있다.")
    void list() {
        // given
        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup menuGroup2 = MenuGroupFixture.createDefaultWithoutId();
        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(menuGroup1, menuGroup2);
    }
}
