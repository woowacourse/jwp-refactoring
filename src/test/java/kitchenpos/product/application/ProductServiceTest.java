package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.TestFixtures;
import kitchenpos.product.application.dto.ProductInformationRequest;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.dto.ProductResponses;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("상품")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create() {
        final ProductRequest request = new ProductRequest("컵누들", 1500L);

        final Product savedProduct = Product.builder()
                .name("컵누들")
                .price(BigDecimal.valueOf(1500))
                .id(1L)
                .build();

        when(productRepository.save(any())).thenReturn(savedProduct);

        final ProductResponse actual = productService.create(request);
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getPrice()).isEqualTo(1500),
                () -> assertThat(actual.getName()).isEqualTo("컵누들")
        );
    }

    @DisplayName("상품의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
        final ProductRequest request = new ProductRequest("컵누들", -1L);

        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 비어있어서는 안 된다")
    @Test
    void createExceptionPriceNull() {
        final ProductRequest request = new ProductRequest("컵누들", null);

        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Product product1 = TestFixtures.createProduct(1L);
        final Product product2 = TestFixtures.createProduct(2L);
        final List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        final ProductResponses actual = productService.list();
        assertThat(actual.getProducts()).usingRecursiveComparison()
                .isEqualTo(Arrays.asList(new ProductResponse(product1), new ProductResponse(product2)));
    }

    @DisplayName("상품의 가격을 변경한다")
    @Test
    void update() {
        final ProductInformationRequest request = new ProductInformationRequest("이름변경", 30000L);
        final Product product = TestFixtures.createProduct();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(TestFixtures.updateProduct(product, request));

        final ProductResponse actual = productService.update(1L, request);

        assertThat(actual.getPrice()).isEqualTo(request.getPrice());
    }
}
