package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("반반");

        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @DisplayName("전체 메뉴 그룹을 조회한다")
    @Test
    void findAll() {
        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(4);
    }
}
