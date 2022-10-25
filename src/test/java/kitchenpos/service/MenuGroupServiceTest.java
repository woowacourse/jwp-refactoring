package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Transactional
public class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;

    public MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void createMenuGroup() {

        assertDoesNotThrow(() -> menuGroupService.create(new MenuGroup("밥류")));
    }

    @DisplayName("생성된 메뉴 그룹을 조회한다.")
    @Test
    public void listMenuGroup() {
        menuGroupService.create(new MenuGroup("밥류"));
        menuGroupService.create(new MenuGroup("햄류"));
        assertThat(menuGroupService.list()).hasSize(2);
    }
}
