package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.supports.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 그룹 서비스 테스트")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다")
    @Test
    void createMenuGroup() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("주인장 최애 메뉴");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertSoftly(softly -> {
            assertThat(savedMenuGroup.getId()).isPositive();
            assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다")
    @Test
    void findAllMenuGroups() {
        // given
        menuGroupService.create(MenuGroupFixture.create());
        menuGroupService.create(MenuGroupFixture.create());

        // when
        final List<MenuGroup> list = menuGroupService.list();

        // then
        assertThat(list).hasSize(2);
    }
}
