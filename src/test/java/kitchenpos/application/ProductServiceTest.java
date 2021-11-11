package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.exception.InvalidProductException;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Product Service 테스트")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("Product를 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 Product는 저장에 성공한다.")
        @Test
        void success() {
            // given
            ProductRequest request = ProductRequest를_생성한다("치즈버거", 4_500);

            // when
            ProductResponse response = productService.create(request);

            // then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getPrice().compareTo(request.getPrice())).isEqualTo(0);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            ProductRequest request = ProductRequest를_생성한다(null, 4_500);

            // when, then
            assertThatThrownBy(() -> productService.create(request))
                .isExactlyInstanceOf(InvalidProductException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            ProductRequest request = ProductRequest를_생성한다("치킨버거", null);

            // when, then
            assertThatThrownBy(() -> productService.create(request))
                .isExactlyInstanceOf(InvalidProductException.class);
        }

        @DisplayName("price가 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // given
            ProductRequest request = ProductRequest를_생성한다("치킨버거", -1);

            // when, then
            assertThatThrownBy(() -> productService.create(request))
                .isExactlyInstanceOf(InvalidProductException.class);
        }
    }

    @DisplayName("모든 Product를 조회한다.")
    @Test
    void findAll() {
        // given
        List<ProductResponse> beforeSavedResponses = productService.list();

        beforeSavedResponses.add(productService.create(ProductRequest를_생성한다("치즈버거", 4_500)));
        beforeSavedResponses.add(productService.create(ProductRequest를_생성한다("치킨버거", 5_000)));
        beforeSavedResponses.add(productService.create(ProductRequest를_생성한다("주는대로 먹어", 100_000)));

        // when
        List<ProductResponse> afterSavedResponses = productService.list();

        // then
        assertThat(afterSavedResponses).hasSize(beforeSavedResponses.size());
        assertThat(afterSavedResponses).usingRecursiveComparison()
            .ignoringFields("price")
            .isEqualTo(beforeSavedResponses);
    }

    private ProductRequest ProductRequest를_생성한다(String name, int price) {
        return ProductRequest를_생성한다(name, BigDecimal.valueOf(price));
    }

    private ProductRequest ProductRequest를_생성한다(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
