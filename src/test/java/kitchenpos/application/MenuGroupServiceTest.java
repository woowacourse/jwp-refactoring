package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixtures.Fixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    Fixtures fixtures;

    @Test
    void 메뉴그룹을_등록한다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("세트메뉴");
        menuGroup.setId(1L);

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    void 모든_메뉴그룹_목록을_불러온다() {
        // given
        MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }


}
