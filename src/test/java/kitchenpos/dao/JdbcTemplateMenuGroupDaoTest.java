package kitchenpos.dao;

import kitchenpos.common.repository.RepositoryTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateMenuGroupDaoTest extends RepositoryTest {

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
