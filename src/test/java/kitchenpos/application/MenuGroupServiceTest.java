package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_등록한다() {
        MenuGroup 메뉴_그룹 = 새로운_메뉴_그룹("메뉴 그룹");

        MenuGroup 저장된_메뉴_그룹 = menuGroupService.create(메뉴_그룹);

        assertSoftly(softly -> {
                    assertThat(저장된_메뉴_그룹.getId()).isNotNull();
                    assertThat(저장된_메뉴_그룹).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(메뉴_그룹);
                }
        );
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        MenuGroup 메뉴_그룹1 = menuGroupService.create(새로운_메뉴_그룹("메뉴 그룹1"));
        MenuGroup 메뉴_그룹2 = menuGroupService.create(새로운_메뉴_그룹("메뉴 그룹2"));

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(메뉴_그룹1, 메뉴_그룹2);
    }
}
