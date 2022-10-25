package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME2;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupDaoTest extends DaoTest {

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("MenuGroup을 저장한다.")
    void save() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));

        assertThat(menuGroup).isEqualTo(menuGroupDao.findById(menuGroup.getId()).orElseThrow());
    }

    @Test
    @DisplayName("MenuGroup을 모두 조회한다.")
    void findAll() {
        MenuGroup menuGroup1 = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        MenuGroup menuGroup2 = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME2));

        assertThat(menuGroupDao.findAll()).containsExactly(menuGroup1, menuGroup2);
    }

    @Test
    @DisplayName("MenuGroup이 존재하는지 확인한다.")
    void existsById() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));

        assertThat(menuGroupDao.existsById(menuGroup.getId())).isTrue();
    }
}
