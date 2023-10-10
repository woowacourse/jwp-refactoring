package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JdbcTemplateMenuProductDao.class)
@JdbcTest
class JdbcTemplateMenuProductDaoTest {

    @Autowired
    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    @Test
    void 저장한다() {
        // given
        int beforeSize = jdbcTemplateMenuProductDao.findAll().size();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);
        menuProduct.setMenuId(1L);

        // when
        jdbcTemplateMenuProductDao.save(menuProduct);

        // then
        int afterSize = jdbcTemplateMenuProductDao.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    void 메뉴_아이디로_상품들을_조회한다() {
        // given
        Long fixtureId = 1L;

        // when
        List<MenuProduct> result = jdbcTemplateMenuProductDao.findAllByMenuId(fixtureId);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 모두_조회한다() {
        // given
        int fixtureSize = 6;

        // when
        List<MenuProduct> result = jdbcTemplateMenuProductDao.findAll();

        // then
        assertThat(result).hasSize(fixtureSize);
    }
}
