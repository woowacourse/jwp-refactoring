package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("신제품"));

        assertThat(menuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        menuGroupService.create(new MenuGroup("신제품"));

        assertThat(menuGroupService.list()).hasSize(1);
    }
}
