package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.inmemorydao.InMemoryMenuGroupDao;

class MenuGroupServiceTest {
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new InMemoryMenuGroupDao();
        this.menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() {
        // Given
        final MenuGroup menuGroup = new MenuGroup();

        // When
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // Then
        assertThat(createdMenuGroup)
                .extracting(MenuGroup::getId)
                .isNotNull()
        ;
    }

    @DisplayName("메뉴 그룹의 목록을 조회한다")
    @Test
    void list() {
        // Given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroupService.create(menuGroup);

        // When
        final List<MenuGroup> list = menuGroupService.list();

        // Then
        assertThat(list).isNotEmpty();
    }
}
