package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ServiceIntegrationTest;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.vo.exception.PriceException;
import kitchenpos.vo.exception.PriceExceptionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("Product를 추가한다.")
    class Create {

        @Test
        @DisplayName("정상적으로 추가된다.")
        void success() {
            final ProductDto productDto = ProductFixture.후라이드_DTO();

            final ProductDto savedProductDto = productService.create(productDto);

            assertAll(
                () -> assertThat(savedProductDto.getPrice())
                    .isEqualByComparingTo(productDto.getPrice()),
                () -> assertThat(savedProductDto.getName())
                    .isEqualTo(productDto.getName())
            );
        }

        @Test
        @DisplayName("price가 null인 경우 예외처리한다..")
        void throwExceptionPriceIsNull() {
            final ProductDto product = new ProductDto(null, "test", null);

            Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(PriceException.class)
                .hasMessage(PriceExceptionType.PRICE_IS_NULL.getMessage());
        }

        @Test
        @DisplayName("price가 0보다 작은 Exception을 throw한다.")
        void throwExceptionPriceIsLowerThan0() {
            final ProductDto productDto = new ProductDto(null, "test", BigDecimal.valueOf(-1));

            Assertions.assertThatThrownBy(() -> productService.create(productDto))
                .isInstanceOf(PriceException.class)
                .hasMessage(PriceExceptionType.PRICE_IS_LOWER_THAN_ZERO.getMessage());
        }
    }

    @Test
    @DisplayName("product 목록을 반환할 수 있다.")
    void list() {
        final ProductDto savedProduct = productService.create(ProductFixture.간장치킨_DTO());

        final List<ProductDto> products = productService.list();

        Assertions.assertThat(products)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("price")
            .containsExactlyInAnyOrder(
                savedProduct
            );
    }
}
