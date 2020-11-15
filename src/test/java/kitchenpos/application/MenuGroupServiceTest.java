package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest extends AbstractServiceTest {
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(new JdbcTemplateMenuGroupDao(dataSource));
    }

    @DisplayName("메뉴 그룹을 정상적으로 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = MenuGroupFixture.createWithoutId();
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(savedMenuGroup).isEqualToIgnoringGivenFields(menuGroup, "id"),
            () -> assertThat(savedMenuGroup.getId()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 그룹들의 리스트를 정상적으로 반환한다.")
    @Test
    void list() {
        MenuGroup menuGroup = MenuGroupFixture.createWithoutId();
        List<MenuGroup> expected = Arrays.asList(
            menuGroupService.create(menuGroup),
            menuGroupService.create(menuGroup),
            menuGroupService.create(menuGroup)
        );
        List<MenuGroup> actual = menuGroupService.list();

        assertThat(expected)
            .usingFieldByFieldElementComparator()
            .containsAll(actual);
    }
}