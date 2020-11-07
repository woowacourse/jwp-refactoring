package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class MenuGroupDaoTest {

    MenuGroupDao menuGroupDao;

    MenuGroup menuGroup;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);

        menuGroup = MenuGroupFixture.createWithoutId();
    }

    @DisplayName("Insert a menu group")
    @Test
    void save() {
        MenuGroup saved = menuGroupDao.save(menuGroup);

        assertThat(saved).isEqualToIgnoringGivenFields(menuGroup, "id");
        assertThat(saved).extracting(MenuGroup::getId).isEqualTo(1L);
    }

    @DisplayName("Select a menu group")
    @Test
    void findById() {
        MenuGroup saved = menuGroupDao.save(menuGroup);

        assertThat(menuGroupDao.findById(saved.getId()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("Select all menu group")
    @Test
    void findAll() {
        MenuGroup saved1 = menuGroupDao.save(menuGroup);
        MenuGroup saved2 = menuGroupDao.save(menuGroup);

        assertThat(menuGroupDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("id로 menu group이 존재하는 지 확인")
    @Test
    void existsById() {
        MenuGroup saved1 = menuGroupDao.save(menuGroup);

        assertThat(menuGroupDao.existsById(saved1.getId())).isTrue();
    }
}
