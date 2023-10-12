package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("상품은 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final Product product = new Product(1L, "떡볶이", new BigDecimal("4500.00"));

            // when
            final Product savedProduct = productService.create(product);

            // then
            assertAll(
                    () -> assertThat(savedProduct.getId()).isEqualTo(1L),
                    () -> assertThat(savedProduct.getName()).isEqualTo("떡볶이"),
                    () -> assertThat(savedProduct.getPrice()).isEqualTo("4500.00")
            );
        }

        @Test
        @DisplayName("가격이 비어있으면 예외가 발생한다.")
        void throwsExceptionWhenPriceIsNull() {
            // given
            final Product product = new Product(1L, "떡볶이", null);

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE, -999999})
        @DisplayName("가격이 0보다 작은 경우 예외가 발생한다.")
        void throwsExceptionWhenPriceIsUnderZero(int price) {
            // given
            final Product product = new Product(1L, "떡볶이", new BigDecimal(price));

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }


    @Test
    @DisplayName("상품 목록을 정상적으로 조회한다.")
    void list() {
        // given
        final Product productA = new Product(1L, "라면", new BigDecimal("4000.00"));
        final Product productB = new Product(2L, "순대", new BigDecimal("4500.00"));
        productDao.save(productA);
        productDao.save(productB);

        // when
        final List<Product> products = productService.list();

        // then
        final Product savedProductA = products.get(0);
        final Product savedProductB = products.get(1);
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(savedProductA.getId()).isEqualTo(1L),
                () -> assertThat(savedProductA.getName()).isEqualTo("라면"),
                () -> assertThat(savedProductA.getPrice()).isEqualTo("4000.00"),
                () -> assertThat(savedProductB.getId()).isEqualTo(2L),
                () -> assertThat(savedProductB.getName()).isEqualTo("순대"),
                () -> assertThat(savedProductB.getPrice()).isEqualTo("4500.00")
        );
    }
}
