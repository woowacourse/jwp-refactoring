package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestEntityFactory;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Transactional
public class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;
    private final TestEntityFactory testEntityFactory;

    public MenuGroupServiceTest(MenuGroupService menuGroupService, TestEntityFactory testEntityFactory) {
        this.menuGroupService = menuGroupService;
        this.testEntityFactory = testEntityFactory;
    }

    @DisplayName("메뉴 분류를 생성한다.")
    @Test
    public void createMenuGroup() {
        MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroupRequest("밥류"));

        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("생성된 메뉴 분류를 조회한다.")
    @Test
    public void listMenuGroup() {
        testEntityFactory.메뉴_분류를_생성한다("밥류");
        testEntityFactory.메뉴_분류를_생성한다("햄류");

        assertThat(menuGroupService.list()).hasSize(2);
    }
}
