package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class MenuProductDaoTest {

    @Autowired
    DataSource dataSource;

    MenuProductDao menuProductDao;

    MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);

        menuProduct = MenuProductFixture.create(1L,1L, 1);
    }

    @DisplayName("Insert a menu product")
    @Test
    void save() {
        MenuProduct saved = menuProductDao.save(menuProduct);

        assertThat(saved).isEqualToIgnoringGivenFields(menuProduct, "seq");
        assertThat(saved).extracting(MenuProduct::getSeq).isEqualTo(1L);
    }

    @DisplayName("Select a menu product")
    @Test
    void findById() {
        MenuProduct saved = menuProductDao.save(menuProduct);

        assertThat(menuProductDao.findById(saved.getSeq()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("Select all menu products")
    @Test
    void findAll() {
        MenuProduct saved1 = menuProductDao.save(menuProduct);
        MenuProduct saved2 = menuProductDao.save(menuProduct);

        assertThat(menuProductDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Select all menu products by menu id")
    @Test
    void findAllByMenuId() {
        MenuProduct saved1 = menuProductDao.save(menuProduct);
        MenuProduct saved2 = menuProductDao.save(menuProduct);

        assertThat(menuProductDao.findAllByMenuId(saved1.getMenuId()))
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }
}
