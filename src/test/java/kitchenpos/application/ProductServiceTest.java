package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.ui.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() {
        ProductRequest request = new ProductRequest(PRODUCT_NAME1, PRODUCT_PRICE);
        when(productRepository.save(any())).thenReturn(createProduct1(1L));

        assertDoesNotThrow(() -> productService.create(request));
    }

    @DisplayName("상품의 가격이 0원 미만일 경우 생성할 수 없다.")
    @Test
    void createExceptionIfPriceZero() {
        ProductRequest request = new ProductRequest(PRODUCT_NAME1, BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        Product product1 = createProduct1(1L);
        Product product2 = createProduct1(2L);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> actual = productService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(product1, product2)
        );
    }
}
