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

import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuProductFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class MenuProductDaoTest {

    private MenuProductDao menuProductDao;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);

        menuProduct = MenuProductFixture.create(1L, 1L);
    }

    @DisplayName("Menu Product를 저장한다.")
    @Test
    void save() {
        MenuProduct saved = menuProductDao.save(menuProduct);

        assertThat(saved).isEqualToIgnoringGivenFields(menuProduct, "seq");
        assertThat(saved).extracting(MenuProduct::getSeq).isEqualTo(1L);
    }

    @DisplayName("Seq에 해당하는 Menu Product를 조회한다.")
    @Test
    void findById() {
        MenuProduct saved = menuProductDao.save(menuProduct);

        assertThat(menuProductDao.findById(saved.getSeq()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("모든 Menu Product를 조회한다.")
    @Test
    void findAll() {
        MenuProduct saved1 = menuProductDao.save(menuProduct);
        MenuProduct saved2 = menuProductDao.save(menuProduct);

        assertThat(menuProductDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Menu Id에 해당하는 Menu Product를 모두 조회한다.")
    @Test
    void findAllByMenuId() {
        MenuProduct saved1 = menuProductDao.save(menuProduct);
        MenuProduct saved2 = menuProductDao.save(menuProduct);

        assertThat(menuProductDao.findAllByMenuId(saved1.getMenuId()))
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }
}
