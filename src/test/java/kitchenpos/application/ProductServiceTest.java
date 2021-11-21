package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.factory.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

@MockitoSettings
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("모든 Product 를 조회한다")
    @Test
    void list() {
        // given
        Product product = ProductFactory.builder()
            .id(1L)
            .name("닭")
            .price(new BigDecimal(1000))
            .build();
        given(productRepository.findAll()).willReturn(Collections.singletonList(product));

        // when
        final List<ProductResponse> result = productService.list();

        // then
        assertThat(result).first()
            .usingRecursiveComparison()
            .isEqualTo(product);
    }

    @Nested
    class CreateTest {

        private Product product;

        private Long savedProductId;

        private Product savedProduct;

        private ProductRequest productRequest;

        @BeforeEach
        void setUp() {
            product = ProductFactory.builder()
                .price(new BigDecimal(17000))
                .build();

            savedProductId = 1L;

            savedProduct = ProductFactory.copy(product)
                .id(1L)
                .build();

            productRequest = ProductFactory.dto(product);
        }

        @DisplayName("Product 를 생성한다")
        @Test
        void create() {
            // given
            given(productRepository.save(any(Product.class))).willAnswer(
                invocation -> {
                    Product toSave = invocation.getArgument(0);
                    ReflectionTestUtils.setField(toSave, "id", savedProductId);
                    return null;
                }
            );

            // when
            ProductResponse result = productService.create(productRequest);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedProduct);
        }

        @DisplayName("Product 생성 실패한다 - product 의 price 가 null 인 경우")
        @Test
        void create_whenPriceIsNull() {
            // given
            product = ProductFactory.copy(product)
                .price(null)
                .build();
            productRequest = ProductFactory.dto(product);

            // when
            ThrowingCallable throwingCallable = () -> productService.create(productRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Product 생성 실패한다 - product 의 price 가 음수인 경우")
        @Test
        void create_whenPriceIsNegative() {
            // given
            product = ProductFactory.copy(product)
                .price(new BigDecimal(-1))
                .build();
            productRequest = ProductFactory.dto(product);

            // when
            ThrowingCallable throwingCallable = () -> productService.create(productRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
