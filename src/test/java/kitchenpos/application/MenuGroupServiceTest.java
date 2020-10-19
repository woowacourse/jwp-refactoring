package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹 저장")
    @Test
    void createTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo("한마리메뉴")
        );
    }

    @DisplayName("저장된 모든 메뉴 그룹 반환")
    @Test
    void listTest() {
        saveMenuGroup(menuGroupRepository, "한마리메뉴");
        saveMenuGroup(menuGroupRepository, "두마리메뉴");

        List<MenuGroup> allMenuGroups = menuGroupService.list();

        assertThat(allMenuGroups).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        menuGroupRepository.deleteAll();
    }
}