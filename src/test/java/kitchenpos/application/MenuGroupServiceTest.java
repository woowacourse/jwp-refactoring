package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.test.fixtures.MenuGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.BASIC.get();
        final MenuGroup empty = MenuGroupFixtures.EMPTY.get();

        // when
        final MenuGroup saved = menuGroupService.create(menuGroup);

        // then
        final MenuGroup actualMenuGroup = menuGroupDao.findById(saved.getId()).orElse(empty);
        assertThat(actualMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회")
    void getMenuGroups() {
        // given
        menuGroupService.create(MenuGroupFixtures.BASIC.get());

        // when
        final List<MenuGroup> actualMenuGroups = menuGroupService.list();

        // then
        assertThat(actualMenuGroups).isNotNull();
    }
}
