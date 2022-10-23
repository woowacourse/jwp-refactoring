package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateMenuProductDaoTest {

    private final MenuProductDao menuProductDao;

    @Autowired
    JdbcTemplateMenuProductDaoTest(final DataSource dataSource) {
        this.menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @Test
    void 저장한다() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(1);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);

        // when
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
                () -> assertThat(savedMenuProduct.getQuantity()).isEqualTo(1),
                () -> assertThat(savedMenuProduct.getMenuId()).isEqualTo(1L),
                () -> assertThat(savedMenuProduct.getProductId()).isEqualTo(1L)
        );
    }

    @Test
    void ID로_조회한다() {
        // given
        Long id = 1L;

        // when
        Optional<MenuProduct> menuProduct = menuProductDao.findById(id);

        // then
        Assertions.assertAll(
                () -> assertThat(menuProduct).isPresent(),
                () -> assertThat(menuProduct.get())
                        .usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(new MenuProduct(null, 1L, 1L, 1))
        );
    }

    @Test
    void 일치하는_ID가_없을_경우_empty를_반환한다() {
        // given
        Long notExistId = -1L;

        // when
        Optional<MenuProduct> menuProduct = menuProductDao.findById(notExistId);

        // then
        assertThat(menuProduct).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        // then
        assertThat(menuProducts).hasSize(6)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(
                                new MenuProduct(null, 1L, 1L, 1),
                                new MenuProduct(null, 2L, 2L, 1),
                                new MenuProduct(null, 3L, 3L, 1),
                                new MenuProduct(null, 4L, 4L, 1),
                                new MenuProduct(null, 5L, 5L, 1),
                                new MenuProduct(null, 6L, 6L, 1)
                        )
                );
    }

    @Test
    void menu_id로_조회한다() {
        // given
        long menuId = 1L;

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuId);

        // then
        assertThat(menuProducts).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(
                                new MenuProduct(null, 1L, 1L, 1)
                        )
                );
    }
}
