package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductDaoTest {

    private final ProductDao productDao;

    @Autowired
    public ProductDaoTest(final DataSource dataSource) {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("상품을 저장한다")
    void save() {
        // given
        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("듀오 치킨"),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(new BigDecimal(3000))
        );
    }

    @Test
    @DisplayName("id로 상품을 조회한다")
    void findById() {
        // given
        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));
        final Product savedProduct = productDao.save(product);

        // when
        final Product foundProduct = productDao.findById(savedProduct.getId())
                .get();

        // then
        assertThat(foundProduct).usingRecursiveComparison()
                .isEqualTo(savedProduct);
    }

    @Test
    @DisplayName("id로 상품을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<Product> product = productDao.findById(-1L);

        // then
        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("모든 상품을 조회한다")
    void findAll() {
        // given
        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));
        final Product savedProduct = productDao.save(product);

        // when
        final List<Product> products = productDao.findAll();

        // then
        assertAll(
                () -> assertThat(products).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(products).extracting("id")
                        .contains(savedProduct.getId())
        );
    }
}
