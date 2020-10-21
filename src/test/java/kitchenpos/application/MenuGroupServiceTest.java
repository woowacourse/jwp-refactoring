package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Transactional
    @DisplayName("새로운 MenuGroup을 추가할 수 있다.")
    @Test
    void createMenuGroup() {
        MenuGroup menuGroup = OrderServiceTest.createMenuGroup("회식세트메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(() -> {
            assertThat(savedMenuGroup.getId()).isNotNegative();
            assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @DisplayName("예외: 이름이 없는 MenuGroup을 추가")
    @Test
    void createMenuGroupWithoutName() {
        MenuGroup menuGroup = OrderServiceTest.createMenuGroup(null);

        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Transactional
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    @Test
    void findAllMenuGroups() {
        MenuGroup chickenMenu = OrderServiceTest.createMenuGroup("치킨메뉴");
        MenuGroup springOnionChickenMenu = OrderServiceTest.createMenuGroup("파닭메뉴");
        MenuGroup newMenu = OrderServiceTest.createMenuGroup("신메뉴");
        menuGroupDao.save(chickenMenu);
        menuGroupDao.save(springOnionChickenMenu);
        menuGroupDao.save(newMenu);

        List<MenuGroup> list = menuGroupService.list();

        assertAll(() -> {
            assertThat(list).hasSize(3);
            assertThat(list).extracting(MenuGroup::getName)
                    .containsOnly(chickenMenu.getName(), springOnionChickenMenu.getName(), newMenu.getName());
        });
    }
}