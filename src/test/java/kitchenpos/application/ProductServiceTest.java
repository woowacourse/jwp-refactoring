package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("생성하려는 Product의 Price가 null이면")
        @Nested
        class Context_price_null {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                ProductRequestDto productRequestDto = new ProductRequestDto("참치마요", null);

                // when, then
                assertThatCode(() -> productService.create(productRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("유효하지 않은 Product 가격입니다.");
            }
        }

        @DisplayName("생성하려는 Product의 Price가 음수면")
        @Nested
        class Context_price_negative {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                ProductRequestDto productRequestDto = new ProductRequestDto("참치마요", BigDecimal.valueOf(-1));

                // when, then
                assertThatCode(() -> productService.create(productRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("유효하지 않은 Product 가격입니다.");
            }
        }

        @DisplayName("생성하려는 Product의 Price가 0 이상 양수면")
        @Nested
        class Context_price_positive {

            @DisplayName("정상적으로 Product를 생성 및 저장한다.")
            @ParameterizedTest
            @ValueSource(ints = {0, 300})
            void it_saves_and_returns_product(int price) {
                // given
                ProductRequestDto productRequestDto = new ProductRequestDto("참치마요", BigDecimal.valueOf(price));
                Product expected = new Product(1L, "참치마요", BigDecimal.valueOf(price));
                given(productRepository.save(any(Product.class))).willReturn(expected);

                // when
                ProductResponseDto response = productService.create(productRequestDto);

                // then
                assertThat(response).usingRecursiveComparison()
                    .isEqualTo(new ProductResponseDto(1L, "참치마요", BigDecimal.valueOf(price)));
            }
        }
    }
}
