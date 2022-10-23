package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("새로운 메뉴 그룹을 생성한다.")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회한다.")
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).isNotNull();
    }
}
