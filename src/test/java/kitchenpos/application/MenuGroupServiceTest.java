package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        MenuGroup menuGroup = 메뉴_그룹을_생성한다("메뉴 그룹");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroup)
        );
    }

    @Test
    void 메뉴_그룹의_목록을_조회할_수_있다() {
        MenuGroup menuGroup1 = menuGroupService.create(메뉴_그룹을_생성한다("메뉴 그룹1"));
        MenuGroup menuGroup2 = menuGroupService.create(메뉴_그룹을_생성한다("메뉴 그룹2"));

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(menuGroup1, menuGroup2);
    }
}