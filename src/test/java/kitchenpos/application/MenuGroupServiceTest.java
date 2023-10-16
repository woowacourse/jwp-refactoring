package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.fake.FakeMenuGroupDao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest {

    private MenuGroupService menuGroupService = new MenuGroupService(new FakeMenuGroupDao());

    @Test
    void 메뉴그룹_생성시_저장한_객체와_같은_객체를_반환한다() {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(menuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedMenuGroup);
    }

    @Test
    void 메뉴그룹_전체_조회를_검증한다() {
        menuGroupService.create(new MenuGroup("메뉴그룹1"));
        menuGroupService.create(new MenuGroup("메뉴그룹2"));
        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).hasSize(2);
    }
}
