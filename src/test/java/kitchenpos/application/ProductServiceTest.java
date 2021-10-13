package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

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
                Product product = new Product();
                product.setName("참치마요");
                product.setPrice(null);

                // when, then
                assertThatCode(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("생성하려는 Product의 Price가 음수면")
        @Nested
        class Context_price_negative {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Product product = new Product();
                product.setName("참치마요");
                product.setPrice(BigDecimal.valueOf(-1));

                // when, then
                assertThatCode(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("생성하려는 Product의 Price가 양수면")
        @Nested
        class Context_price_positive {

            @DisplayName("정상적으로 Product를 생성 및 저장한다.")
            @Test
            void it_saves_and_returns_product() {
                // given
                Product product = new Product();
                product.setName("참치마요");
                product.setPrice(BigDecimal.valueOf(300));
                Product expected = new Product();
                expected.setId(1L);
                expected.setName("참치마요");
                given(productDao.save(product)).willReturn(expected);

                // when
                Product response = productService.create(product);

                // then
                assertThat(response).usingRecursiveComparison()
                    .isEqualTo(expected);
            }
        }
    }
}
