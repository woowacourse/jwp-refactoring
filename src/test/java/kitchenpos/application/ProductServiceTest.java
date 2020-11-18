package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.FixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성")
    @Test
    void create() {
        Product productRequest = createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L));
        Product savedProduct = productService.create(productRequest);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(productRequest.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(productRequest.getPrice())
        );
    }

    @DisplayName("상품의 가격이 null, 음수일 경우 예외 발생")
    @ParameterizedTest
    @MethodSource("kitchenpos.fixture.FixtureMethod#provideNullAndNegativeLongValue")
    void create_exception1(BigDecimal invalidPrice) {
        Product productRequest = createProduct(null, "터틀치킨", invalidPrice);

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        Product productRequest = createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L));
        productService.create(productRequest);

        List<Product> savedProducts = productService.list();

        assertThat(savedProducts.size()).isEqualTo(1);
    }
}