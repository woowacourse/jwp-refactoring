import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 등록")
    @ValueSource(ints = {0, 1, 17000})
    @ParameterizedTest
    void create(int price) {
        Long generatedId = 1L;
        Mockito.when(productRepository.save(
            ArgumentMatchers.any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return new Product(generatedId, product.getName(), product.getPrice());
        });

        ProductRequest request = new ProductRequest("강정치킨", BigDecimal.valueOf(price));
        ProductResponse actual = productService.create(request);
        ProductResponse expected = ProductResponse.from(
            new Product(generatedId, request.getName(), request.getPrice())
        );

        Mockito.verify(productRepository, Mockito.times(1))
            .save(ArgumentMatchers.any(Product.class));
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
        Mockito.verify(productRepository, Mockito.times(0))
            .save(ArgumentMatchers.any(Product.class));
    }

    @DisplayName("상품 조회")
    @Test
    void list() {
        List<Product> products = Arrays.asList(
            new Product(1L, "후라이드", BigDecimal.valueOf(16000)),
            new Product(2L, "양념치킨", BigDecimal.valueOf(16000)),
            new Product(3L, "반반치킨", BigDecimal.valueOf(16000))
        );
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> actual = productService.list();
        List<ProductResponse> expected = ProductResponse.listFrom(products);

        Mockito.verify(productRepository, Mockito.times(1)).findAll();
        assertThat(actual).hasSameSizeAs(expected)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expected);
    }
}
