package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateProductDaoTest {

    private final ProductDao productDao;

    @Autowired
    public JdbcTemplateProductDaoTest(final DataSource dataSource) {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    void 저장한다() {
        // given
        Product product = new Product();
        product.setName("pasta");
        product.setPrice(BigDecimal.valueOf(13000));
        // when
        Product savedProduct = productDao.save(product);

        // then
        Assertions.assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("pasta")
        );
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<Product> products = productDao.findAll();

        // then
        assertThat(products).hasSize(6)
                .usingRecursiveComparison()
                .ignoringFields("price")
                .isEqualTo(Arrays.asList(
                                new Product(1L, "후라이드", BigDecimal.valueOf(16000)),
                                new Product(2L, "양념치킨", BigDecimal.valueOf(16000)),
                                new Product(3L, "반반치킨", BigDecimal.valueOf(16000)),
                                new Product(4L, "통구이", BigDecimal.valueOf(16000)),
                                new Product(5L, "간장치킨", BigDecimal.valueOf(17000)),
                                new Product(6L, "순살치킨", BigDecimal.valueOf(17000))
                        )
                );
    }

    @Test
    void ID로_상품을_조회한다() {
        // given
        Long id = 1L;

        // when
        Optional<Product> product = productDao.findById(id);

        // then
        Assertions.assertAll(
                () -> assertThat(product).isPresent(),
                () -> assertThat(product.get().getName()).isEqualTo("후라이드")
        );
    }
}
