package kitchenpos.application;

import static kitchenpos.support.ProductFixture.createProduct;
import static kitchenpos.support.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends IntegrationServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 상품가에_null을_입력할_경우 {

            private final BigDecimal NULL_PRICE = null;
            private final ProductRequest productRequest = new ProductRequest("간장", NULL_PRICE);

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> productService.create(productRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 상품가가_음수인_경우 {

            private final int MINUS_PRICE = -1;

            private final ProductRequest productRequest = createProductRequest(MINUS_PRICE);

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> productService.create(productRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 상품을_정상적으로_생성가능한_경우 {

            private final ProductRequest productRequest = createProductRequest(18_000);

            @Test
            void 저장된_상품이_반환된다() {

                Product actual = productService.create(productRequest);
                assertThat(actual).isNotNull();
            }
        }
    }

    @Nested
    class list_메서드는 extends IntegrationServiceTest {

        @BeforeEach
        void setUp() {
            productRepository.save(createProduct("양념 치킨"));
            productRepository.save(createProduct("간장 치킨"));
            productRepository.save(createProduct("순살 바베큐 치킨"));
        }

        @Test
        void 상품목록을_반환한다() {

            List<Product> actual = productService.list();
            assertThat(actual).hasSize(3);
        }
    }
}
