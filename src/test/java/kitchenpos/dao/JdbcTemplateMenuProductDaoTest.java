package kitchenpos.dao;

import static kitchenpos.common.fixture.MenuFixture.메뉴;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.common.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.common.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@JdbcTest
class JdbcTemplateMenuProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    private Long menuId;
    private Long productId;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuProductDao = new JdbcTemplateMenuProductDao(dataSource);

        JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        Long menuGroupId = jdbcTemplateMenuGroupDao.save(메뉴_그룹()).getId();

        JdbcTemplateMenuDao jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        menuId = jdbcTemplateMenuDao.save(메뉴(menuGroupId)).getId();

        JdbcTemplateProductDao jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
        productId = jdbcTemplateProductDao.save(상품()).getId();
    }

    @Test
    void 메뉴_상품을_저장한다() {
        // given
        MenuProduct menuProduct = 메뉴_상품(menuId, productId);

        // when
        MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

        // then
        assertThat(savedMenuProduct).usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(메뉴_상품(menuId, productId));
    }

    @Test
    void ID로_메뉴_상품을_조회한다() {
        // given
        Long menuProductId = jdbcTemplateMenuProductDao.save(메뉴_상품(menuId, productId)).getSeq();

        // when
        MenuProduct menuProduct = jdbcTemplateMenuProductDao.findById(menuProductId).get();

        // then
        assertThat(menuProduct).usingRecursiveComparison()
                .isEqualTo(메뉴_상품(menuProductId, menuId, productId));
    }

    @Test
    void 전체_메뉴_상품을_조회한다() {
        // given
        Long menuProductId_A = jdbcTemplateMenuProductDao.save(메뉴_상품(menuId, productId)).getSeq();
        Long menuProductId_B = jdbcTemplateMenuProductDao.save(메뉴_상품(menuId, productId)).getSeq();

        // when
        List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAll();

        // then
        assertThat(menuProducts).usingRecursiveComparison()
                .isEqualTo(List.of(
                        메뉴_상품(menuProductId_A, menuId, productId),
                        메뉴_상품(menuProductId_B, menuId, productId)
                ));
    }

    @Test
    void 메뉴_ID로_모든_메뉴_상품을_조회한다() {
        // given
        Long menuProductId_A = jdbcTemplateMenuProductDao.save(메뉴_상품(menuId, productId)).getSeq();
        Long menuProductId_B = jdbcTemplateMenuProductDao.save(메뉴_상품(menuId, productId)).getSeq();

        // when
        List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(menuId);

        // then
        assertThat(menuProducts).usingRecursiveComparison()
                .isEqualTo(List.of(
                        메뉴_상품(menuProductId_A, menuId, productId),
                        메뉴_상품(menuProductId_B, menuId, productId)
                ));
    }
}
