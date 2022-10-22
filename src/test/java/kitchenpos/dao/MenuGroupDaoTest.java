package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.fixtures.DomainFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class MenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

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
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));

        assertThat(menuGroupDao.findAll()).contains(menuGroup);
    }

    @Test
    @DisplayName("MenuGroup이 존재하는지 확인한다.")
    void existsById() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));

        assertThat(menuGroupDao.existsById(menuGroup.getId())).isTrue();
    }
}
