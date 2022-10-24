package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuDao menuDao;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한바리반메뉴");

        // when
        MenuGroup newMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(newMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.size()).isEqualTo(4);
    }
}
