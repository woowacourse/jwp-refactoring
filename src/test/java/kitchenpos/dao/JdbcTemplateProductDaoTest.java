package kitchenpos.dao;

import static kitchenpos.common.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.common.DaoTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    void 상품을_저장한다() {
        // given
        Product product = 상품();

        // when
        Product savedProduct = jdbcTemplateProductDao.save(product);

        // then
        assertThat(savedProduct).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(상품());
    }

    @Test
    void ID로_상품을_조회한다() {
        // given
        Long productId = jdbcTemplateProductDao.save(상품()).getId();

        // when
        Product product = jdbcTemplateProductDao.findById(productId).get();

        // then
        assertThat(product).usingRecursiveComparison()
                .isEqualTo(상품(productId));
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        Long productId_A = jdbcTemplateProductDao.save(상품()).getId();
        Long productId_B = jdbcTemplateProductDao.save(상품()).getId();

        // when
        List<Product> products = jdbcTemplateProductDao.findAll();

        // then
        assertThat(products).usingRecursiveComparison()
                .isEqualTo(List.of(상품(productId_A), 상품(productId_B)));
    }
}
