package kitchenpos.application;

import kitchenpos.Fixture.Fixture;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;


@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_등록한다() {
        MenuGroup menuGroup = Fixture.menuGroupFixture("메뉴 그룹");

        MenuGroup saved = menuGroupService.create(menuGroup);

        assertSoftly(softly -> {
            softly.assertThat(saved.getId()).isNotNull();
            softly.assertThat(saved.getName()).isEqualTo("메뉴 그룹");
        });
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        MenuGroup group1 = menuGroupService.create(Fixture.menuGroupFixture("메뉴 그룹1"));
        MenuGroup group2 = menuGroupService.create(Fixture.menuGroupFixture("메뉴 그룹2"));

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(group1, group2);
    }
}
