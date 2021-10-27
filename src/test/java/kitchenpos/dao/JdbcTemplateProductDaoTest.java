package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
import org.springframework.dao.DataIntegrityViolationException;

@JdbcTest
@DisplayName("Product Dao 테스트")
class JdbcTemplateProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
    }

    @DisplayName("Product를 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 Product는 저장에 성공한다.")
        @Test
        void success() {
            // given
            Product product = Product를_생성한다("더블 새우 버거", 4_000);

            // when
            Product savedProduct = jdbcTemplateProductDao.save(product);

            // then
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getName()).isEqualTo(product.getName());
            assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isEqualTo(0);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            Product product = Product를_생성한다(null, 4_000);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateProductDao.save(product))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            Product product = Product를_생성한다("더블 치즈 버거", null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateProductDao.save(product))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 Product를 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 Product 조회에 성공한다.")
        @Test
        void present() {
            // given
            Product savedProduct = jdbcTemplateProductDao.save(Product를_생성한다("더블 새우 버거", 4_000));

            // when
            Optional<Product> foundProduct = jdbcTemplateProductDao.findById(savedProduct.getId());

            // then
            assertThat(foundProduct).isPresent();
            assertThat(foundProduct.get()).usingRecursiveComparison()
                .isEqualTo(savedProduct);
        }

        @DisplayName("ID가 존재하지 않는다면 Product 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<Product> foundProduct = jdbcTemplateProductDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundProduct).isNotPresent();
        }
    }

    @DisplayName("모든 Product를 조회한다.")
    @Test
    void findAll() {
        // given
        List<Product> beforeSavedProducts = jdbcTemplateProductDao.findAll();

        beforeSavedProducts.add(jdbcTemplateProductDao.save(Product를_생성한다("더블 새우 버거", 4_000)));
        beforeSavedProducts.add(jdbcTemplateProductDao.save(Product를_생성한다("더블 치즈 버거", 5_000)));
        beforeSavedProducts.add(jdbcTemplateProductDao.save(Product를_생성한다("불고기 버거", 3_000)));

        // when
        List<Product> afterSavedProducts = jdbcTemplateProductDao.findAll();

        // then
        assertThat(afterSavedProducts).hasSize(beforeSavedProducts.size());
        assertThat(afterSavedProducts).usingRecursiveComparison()
            .isEqualTo(beforeSavedProducts);
    }

    private Product Product를_생성한다(String name, int price) {
        return Product를_생성한다(name, BigDecimal.valueOf(price));
    }

    private Product Product를_생성한다(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}