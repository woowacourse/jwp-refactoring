package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Product;

@DisplayName("JdbcTemplateProductDao 테스트")
@JdbcTest
@Sql("/dao-test.sql")
@Import(JdbcTemplateProductDao.class)
class JdbcTemplateProductDaoTest {
    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @DisplayName("ProductDao save 테스트")
    @Test
    void save() {
        // Given
        final Product product = new Product();
        product.setName("파닭치킨");
        product.setPrice(BigDecimal.valueOf(18000L));

        // When
        final Product savedProduct = jdbcTemplateProductDao.save(product);

        // Then
        assertAll(
                () -> assertThat(savedProduct)
                        .extracting(Product::getId)
                        .isNotNull()
                ,
                () -> assertThat(savedProduct)
                        .extracting(Product::getName)
                        .isEqualTo(product.getName())
                ,
                () -> assertThat(savedProduct)
                        .extracting(Product::getPrice)
                        .extracting(BigDecimal::longValue)
                        .isEqualTo(product.getPrice().longValue())
        );
    }

    @DisplayName("ProductDao findById 테스트")
    @Test
    void findById() {
        // When
        final Product product = jdbcTemplateProductDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertAll(
                () -> assertThat(product)
                        .extracting(Product::getName)
                        .isEqualTo("후라이드치킨")
                ,
                () -> assertThat(product)
                        .extracting(Product::getPrice)
                        .extracting(BigDecimal::longValue)
                        .isEqualTo(16000L)
        );
    }

    @DisplayName("ProductDao findById Id가 존재하지 않을 경우")
    @Test
    void findById_NotExists() {
        // When
        final Optional<Product> product = jdbcTemplateProductDao.findById(7L);

        // Then
        assertThat(product.isPresent()).isFalse();
    }

    @DisplayName("ProductDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<Product> products = jdbcTemplateProductDao.findAll();

        // Then
        assertThat(products).hasSize(6);
    }
}
