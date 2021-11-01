package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ProductServiceTest extends ServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 등록")
    @ValueSource(ints = {0, 1, 17000})
    @ParameterizedTest
    void create(int price) {
        Long generatedId = 1L;
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return new Product(generatedId, product.getName(), product.getPrice());
        });

        ProductRequest request = new ProductRequest("강정치킨", BigDecimal.valueOf(price));
        ProductResponse actual = productService.create(request);
        ProductResponse expected = ProductResponse.from(
            new Product(generatedId, request.getName(), request.getPrice())
        );

        verify(productRepository, times(1)).save(any(Product.class));
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("상품 등록시 가격이 0 미만이면 예외 처리")
    @ValueSource(ints = {-1, -2, -123456789})
    @ParameterizedTest
    void createWithInvalidPrice(int price) {
        ProductRequest request = new ProductRequest("강정치킨", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(request)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @DisplayName("상품 조회")
    @Test
    void list() {
        List<Product> products = Arrays.asList(
            new Product(1L, "후라이드", BigDecimal.valueOf(16000)),
            new Product(2L, "양념치킨", BigDecimal.valueOf(16000)),
            new Product(3L, "반반치킨", BigDecimal.valueOf(16000))
        );
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> actual = productService.list();
        List<ProductResponse> expected = ProductResponse.listFrom(products);

        verify(productRepository, times(1)).findAll();
        assertThat(actual).hasSameSizeAs(expected)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expected);
    }
}
