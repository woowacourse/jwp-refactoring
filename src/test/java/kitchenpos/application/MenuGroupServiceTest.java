package kitchenpos.application;

import fixture.MenuGroupBuilder;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroup menuGroup = MenuGroupBuilder.init()
                .build();
        MenuGroup created = menuGroupService.create(menuGroup);

        assertThat(created).isNotNull();
    }

    @Test
    void 모든_메뉴그룹을_조회한다() {
        List<MenuGroup> all = menuGroupService.list();

        assertThat(all).hasSize(4);
    }
}
