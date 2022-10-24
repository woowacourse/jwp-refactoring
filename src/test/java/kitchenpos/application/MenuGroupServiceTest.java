package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴그룹을 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup menuGroup = new MenuGroup("추천메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        MenuGroup dbMenuGroup = menuGroupDao.findById(savedMenuGroup.getId())
                        .orElseThrow();
        assertThat(dbMenuGroup).isEqualTo(menuGroup);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).contains(menuGroup);
    }
}
