package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;

@DisplayName("MenuGroup 서비스 테스트")
@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹");

        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹의 목록을 조회한다")
    @Test
    void list() {
        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(4);
    }
}
