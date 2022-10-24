package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuDaoTest extends DaoTest {

    private MenuGroupDao menuGroupDao;
    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    @DisplayName("Menu를 저장한다.")
    void save() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Menu menu = menuDao.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));

        assertThat(menu).isEqualTo(menuDao.findById(menu.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 Menu를 조회한다.")
    void findAll() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Menu menu1 = menuDao.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));
        Menu menu2 = menuDao.save(new Menu(MENU2_NAME, MENU2_PRICE, menuGroup.getId()));

        List<Menu> menus = menuDao.findAll();
        assertThat(menus).containsExactly(menu1, menu2);
    }

    @Test
    void countByIdIn() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Menu menu1 = menuDao.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));
        Menu menu2 = menuDao.save(new Menu(MENU2_NAME, MENU2_PRICE, menuGroup.getId()));

        long count = menuDao.countByIdIn(List.of(menu1.getId(), menu2.getId(), 0L));
        assertThat(count).isEqualTo(2);
    }
}
