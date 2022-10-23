package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@JdbcTest
@DisplayName("ProductDao 테스트")
class ProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;

    @BeforeEach
    void beforeAll() {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void save() {
        // given
        final Product product = new Product("파스타", new BigDecimal(1000));

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("단일 상품을 조회한다.")
    @Test
    void findById() {
        // given
        final Product product = new Product("파스타", new BigDecimal(1000));
        final Product savedProduct = productDao.save(product);

        // when
        final Optional<Product> foundProduct = productDao.findById(savedProduct.getId());

        // then
        assertAll(
                () -> assertThat(foundProduct).isPresent(),
                () -> assertThat(foundProduct.get()).usingRecursiveComparison()
                        .isEqualTo(savedProduct)
        );
    }

    @DisplayName("전체 상품들을 조회한다.")
    @Test
    void findAll() {
        // given
        final Product product1 = new Product("파스타", new BigDecimal(1000));
        final Product product2 = new Product("피자", new BigDecimal(1000));
        final Product savedProduct1 = productDao.save(product1);
        final Product savedProduct2 = productDao.save(product2);

        // when
        final List<Product> products = productDao.findAll();

        // then
        assertThat(products).usingFieldByFieldElementComparator()
                .containsAll(List.of(savedProduct1, savedProduct2));
    }
}
