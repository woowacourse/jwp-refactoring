package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.utils.TestFixture;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("create: 상품 등록 확인 테스트")
    @Test
    void createTest() {
        final Product product = TestFixture.getProduct(16000);
        when(productDao.save(any())).thenReturn(product);
        final Product actual = productService.create(product);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(product.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @DisplayName("create: 상품 등록 확인 테스트")
    @Test
    void createTestByPriceSmallThenZero() {
        final Product product = TestFixture.getProduct(-1);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("findProducts: 상품 전체 목록 조회 확인 테스트")
    @Test
    void findProductsTest() {
        final Product friedChicken = TestFixture.getProduct(16000);
        when(productDao.findAll()).thenReturn(Collections.singletonList(friedChicken));

        final List<Product> products = productService.list();

        assertThat(products).hasSize(1);
    }
}
