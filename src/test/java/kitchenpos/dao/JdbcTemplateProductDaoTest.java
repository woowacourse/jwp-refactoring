package kitchenpos.dao;

import static kitchenpos.common.ProductFixtures.PRODUCT1;
import static kitchenpos.common.ProductFixtures.PRODUCT1_NAME;
import static kitchenpos.common.ProductFixtures.PRODUCT1_PRICE;
import static kitchenpos.common.ProductFixtures.PRODUCT2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("Product를 영속화한다.")
    void saveProduct() {
        // given
        final Product product = PRODUCT1();

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedProduct.getId()).isNotNull();
            softly.assertThat(savedProduct.getName()).isEqualTo(PRODUCT1_NAME);
            softly.assertThat(savedProduct.getPrice()).isEqualTo(PRODUCT1_PRICE);
        });
    }

    @Nested
    @DisplayName("ProductId에 해당하는 Product를 조회 시")
    class FindById {

        @Test
        @DisplayName("ProductId에 해당하는 Product가 있으면 값을 반환한다.")
        void findById() {
            // given
            final Product product = PRODUCT1();
            final Product savedProduct = productDao.save(product);

            // when
            final Product foundProduct = productDao.findById(savedProduct.getId()).get();

            // then
            assertSoftly(softly -> {
                softly.assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
                softly.assertThat(foundProduct.getName()).isEqualTo(savedProduct.getName());
                softly.assertThat(foundProduct.getPrice()).isEqualTo(savedProduct.getPrice());
            });
        }

        @Test
        @DisplayName("ProductId에 해당하는 Product가 없으면 Empty를 반환한다.")
        void notExist() {
            // given
            final Long notExistProductId = -1L;

            // when
            Optional<Product> optionalProduct = productDao.findById(notExistProductId);

            // then
            assertThat(optionalProduct).isEmpty();
        }
    }

    @Nested
    @DisplayName("모든 Product 조회 시")
    class FindAll {

        @Test
        @DisplayName("여러 개의 값이 있을 경우 모두 반환한다.")
        void findById() {
            // given
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
            jdbcTemplate.execute("truncate table product");
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

            final Product product1 = PRODUCT1();
            final Product product2 = PRODUCT2();
            final Product savedProduct1 = productDao.save(product1);
            final Product savedProduct2 = productDao.save(product2);
            List<Product> expected = List.of(savedProduct1, savedProduct2);

            // when
            List<Product> products = productDao.findAll();

            // then
            assertThat(products).usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(expected);
        }
    }
}
