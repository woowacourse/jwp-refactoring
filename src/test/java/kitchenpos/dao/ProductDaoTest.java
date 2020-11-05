package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.ProductFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class ProductDaoTest {

    @Autowired
    DataSource dataSource;

    ProductDao productDao;

    Product product1;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);

        product1 = ProductFixture.createWithoutId(18000L);
    }

    @DisplayName("Insert a product")
    @Test
    void save() {
        Product savedProduct = productDao.save(product1);

        assertThat(savedProduct).isEqualToIgnoringGivenFields(product1, "id");
        assertThat(savedProduct).extracting(Product::getId).isEqualTo(1L);
    }

    @DisplayName("Select a product by id")
    @Test
    void findById() {
        Product savedProduct = productDao.save(product1);

        assertThat(productDao.findById(savedProduct.getId()).get())
            .isEqualToComparingFieldByField(savedProduct);
    }

    @DisplayName("Select all products")
    @Test
    void findAll() {
        Product savedProduct1 = productDao.save(product1);
        Product savedProduct2 = productDao.save(product1);

        assertThat(productDao.findAll()).usingRecursiveComparison()
            .isEqualTo(Arrays.asList(savedProduct1, savedProduct2));
    }
}
