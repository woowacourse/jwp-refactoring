package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcTemplateMenuProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final MenuProduct menuProduct = jdbcTemplateMenuProductDao.save(new MenuProduct(1L, 2L, 2));

        //then
        assertThat(jdbcTemplateMenuProductDao.findById(menuProduct.getSeq())).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final List<MenuProduct> result = jdbcTemplateMenuProductDao.findAll();

        //then
        assertThat(result).hasSize(6);
    }

    @Test
    void findAllByMenuId() {
        //when
        final List<MenuProduct> result = jdbcTemplateMenuProductDao.findAllByMenuId(1L);

        //then
        assertThat(result).hasSize(1);
    }
}
