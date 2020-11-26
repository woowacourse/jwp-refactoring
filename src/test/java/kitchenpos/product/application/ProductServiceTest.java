package kitchenpos.product.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("id가 없는 상품으로 id가 있는 상품을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final Product expectedProduct = createProduct(1L, "후라이드", 1000);

        given(productRepository.save(any(Product.class))).willReturn(expectedProduct);

        assertThat(productService.create(createProduct(null, "후라이드", 1000)))
            .isEqualToComparingFieldByField(expectedProduct);
    }

    @DisplayName("주문들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<Product> expectedProducts = Collections.singletonList(createProduct(1L, "후라이드", 1000));

        given(productRepository.findAll()).willReturn(expectedProducts);

        assertThat(productService.list()).usingRecursiveComparison()
            .isEqualTo(expectedProducts);
    }
}
