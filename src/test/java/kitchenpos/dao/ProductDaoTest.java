package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;


    @BeforeEach
    void setup() {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    void 제품을_저장할_수_있다() {
        // given
        final Product product = new Product("제품1", new BigDecimal(10000));

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void 제품을_아이디로_조회할_수_있다() {
        // given
        final Product product = new Product("제품1", new BigDecimal(10000));
        final Product savedProduct = productDao.save(product);

        // when
        final Product foundProduct = productDao.findById(savedProduct.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(foundProduct.getName()).isEqualTo("제품1"),
                () -> assertThat(foundProduct.getPrice().intValue()).isEqualTo(10000)
        );
    }

    @Test
    void 제품을_모두_조회할_수_있다() {
        // given
        final int alreadyExistCount = productDao.findAll().size();
        final Product product = new Product("제품1", new BigDecimal(10000));
        final Product savedProduct = productDao.save(product);

        // when
        final List<Product> products = productDao.findAll();

        // then
        assertThat(products).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedProduct);
    }
}
