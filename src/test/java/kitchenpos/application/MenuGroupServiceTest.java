package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupServiceTest extends ServiceBaseTest {

    @Autowired
    protected MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() {
        //given
        final MenuGroup menuGroup = Fixture.menuGroup("오션 메뉴 그룹");

        //when
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertAll(
                () -> assertThat(createdMenuGroup.getId()).isNotNull(),
                () -> assertThat(createdMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    void list() {
        //given
        final MenuGroup menuGroup = Fixture.menuGroup("오션 메뉴 그룹");
        final MenuGroup menuGroup2 = Fixture.menuGroup("동해 메뉴 그룹");
        menuGroupService.create(menuGroup);
        menuGroupService.create(menuGroup2);

        //when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo(menuGroup.getName()),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo(menuGroup2.getName())
        );
    }
}
