package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
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
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        menuGroup.setId(1L);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuGroup);
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(Collections.singletonList(savedMenuGroup));
    }
}
