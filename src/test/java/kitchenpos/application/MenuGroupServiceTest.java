package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹을 등록할 수 있다.")
    @Test
    void create() {
        final MenuGroup menuGroup = new MenuGroup("추천메뉴");

        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        final int originSize = menuGroupDao.findAll().size();
        menuGroupDao.save(new MenuGroup("할인메뉴"));
        menuGroupDao.save(new MenuGroup("맛있는메뉴"));

        final List<MenuGroup> menuGroups = menuGroupService.list();
        final int afterSize = menuGroups.size();

        assertThat(afterSize - originSize).isEqualTo(2);
    }
}
