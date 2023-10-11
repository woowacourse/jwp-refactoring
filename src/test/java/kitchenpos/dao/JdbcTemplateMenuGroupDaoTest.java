package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(new MenuGroup("한식"));

        //then
        assertThat(jdbcTemplateMenuGroupDao.findById(menuGroup.getId())).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final List<MenuGroup> result = jdbcTemplateMenuGroupDao.findAll();

        //then
        assertThat(result).hasSize(4);
    }

    @Test
    void existsById() {
        //given
        final MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(new MenuGroup("한식"));

        //when
        final boolean expect = jdbcTemplateMenuGroupDao.existsById(menuGroup.getId());

        //then
        assertThat(expect).isEqualTo(true);
    }
}
