package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_등록한다() {
        MenuGroup menuGroup = 새로운_메뉴_그룹("메뉴 그룹");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertSoftly(softly -> {
                    assertThat(savedMenuGroup.getId()).isNotNull();
                    assertThat(savedMenuGroup).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(menuGroup);
                }
        );
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        MenuGroup menuGroup1 = menuGroupService.create(새로운_메뉴_그룹("메뉴 그룹1"));
        MenuGroup menuGroup2 = menuGroupService.create(새로운_메뉴_그룹("메뉴 그룹2"));

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(menuGroup1, menuGroup2);
    }
}
