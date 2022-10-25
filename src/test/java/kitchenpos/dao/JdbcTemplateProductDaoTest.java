package kitchenpos.dao;

import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
class JdbcTemplateProductDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("상품을 저장한다.")
        void success() {
            Product product = APPLE_1000.getProduct();

            Product savedProduct = jdbcTemplateProductDao.save(product);

            Long actual = savedProduct.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private Product product;

        @BeforeEach
        void setUp() {
            product = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
        }

        @Test
        @DisplayName("아이디로 상품을 단일 조회한다.")
        void success() {
            Long id = product.getId();

            Product actual = jdbcTemplateProductDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(product);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            jdbcTemplateProductDao.save(APPLE_1000.getProduct());
        }

        @Test
        @DisplayName("상품 전체 목록을 조회한다.")
        void success() {
            List<Product> products = jdbcTemplateProductDao.findAll();

            assertThat(products).hasSize(2);
        }
    }
}
