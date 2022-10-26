package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_추천상품();

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_추천상품();

        menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    public MenuGroup 메뉴_그룹_추천상품() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천상품");
        return menuGroup;
    }
}
