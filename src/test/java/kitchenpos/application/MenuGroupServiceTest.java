package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 테스트")
    @Nested
    class MenuGroupCreateTest {

        @DisplayName("메뉴 그룹을 생성한다.")
        @Test
        void createMenuGroup() {
            //given
            final MenuGroup expected = new MenuGroup();
            expected.setName("name");

            //when
            final MenuGroup actual = menuGroupService.create(expected);

            //then
            assertSoftly(softly -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(expected.getName());
            });
        }
    }

    @DisplayName("메뉴 그룹 조회 테스트")
    @Nested
    class MenuGroupFindTest {

        @DisplayName("메뉴 그룹을 전체 조회한다.")
        @Test
        void findAllMenuGroup() {
            //given
            final MenuGroup menuGroup1 = new MenuGroup();
            menuGroup1.setName("name1");
            final MenuGroup expected1 = testFixtureBuilder.buildMenuGroup(menuGroup1);

            final MenuGroup menuGroup2 = new MenuGroup();
            menuGroup2.setName("name2");
            final MenuGroup expected2 = testFixtureBuilder.buildMenuGroup(menuGroup2);

            //when
            final List<MenuGroup> actual = menuGroupService.list();

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(2);
                softly.assertThat(actual.get(0).getId()).isEqualTo(expected1.getId());
                softly.assertThat(actual.get(0).getName()).isEqualTo(expected1.getName());
                softly.assertThat(actual.get(1).getId()).isEqualTo(expected2.getId());
                softly.assertThat(actual.get(1).getName()).isEqualTo(expected2.getName());
            });
        }
    }
}
