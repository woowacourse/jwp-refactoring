package kitchenpos.application.menugroup;

import kitchenpos.application.MenuGroupService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupQueryServiceTest extends ApplicationTestConfig {

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("전체 메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        final List<MenuGroup> expected = new ArrayList<>();
        for (int productSaveCount = 1; productSaveCount <= 10; productSaveCount++) {
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트 메뉴 그룹명"));
            expected.add(savedMenuGroup);
        }

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
