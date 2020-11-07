package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class ProductDaoTest {

    private ProductDao productDao;

    private Product product1;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        productDao = new JdbcTemplateProductDao(dataSource);

        product1 = ProductFixture.createWithoutId();
    }

    @DisplayName("Product를 저장한다.")
    @Test
    void save() {
        Product savedProduct = productDao.save(product1);

        assertThat(savedProduct)
            .usingComparatorForType(Comparator.comparingDouble(BigDecimal::longValue),
                BigDecimal.class)
            .isEqualToIgnoringGivenFields(product1, "id");
        assertThat(savedProduct).extracting(Product::getId).isEqualTo(savedProduct.getId());
    }

    @DisplayName("Id에 해당하는 Product를 조회한다.")
    @Test
    void findById() {
        Product savedProduct = productDao.save(product1);

        assertThat(productDao.findById(savedProduct.getId()).get())
            .usingComparatorForType(Comparator.comparingDouble(BigDecimal::longValue),
                BigDecimal.class)
            .isEqualToComparingFieldByField(savedProduct);
    }

    @DisplayName("모든 Product를 조회한다.")
    @Test
    void findAll() {
        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product1);

        assertThat(productDao.findAll()).usingRecursiveComparison()
            .isEqualTo(Arrays.asList(savedProduct1, savedProduct2));
    }
}
