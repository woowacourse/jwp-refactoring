package kitchenpos.dao;

import static kitchenpos.common.fixture.ProductFixture.새_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
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
        Product product = 새_상품();

        // when
        Product savedProduct = jdbcTemplateProductDao.save(product);

        // then
        assertThat(savedProduct).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(새_상품());
    }

    @Test
    void ID로_상품을_조회한다() {
        // given
        Long productId = jdbcTemplateProductDao.save(새_상품()).getId();

        // when
        Product foundProduct = jdbcTemplateProductDao.findById(productId).get();

        // then
        assertThat(foundProduct).usingRecursiveComparison()
                .isEqualTo(새_상품(productId));
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        Long productId1 = jdbcTemplateProductDao.save(새_상품()).getId();
        Long productId2 = jdbcTemplateProductDao.save(새_상품()).getId();

        // when
        List<Product> products = jdbcTemplateProductDao.findAll();

        // then
        assertThat(products).usingRecursiveComparison()
                .isEqualTo(List.of(새_상품(productId1), 새_상품(productId2)));
    }
}
