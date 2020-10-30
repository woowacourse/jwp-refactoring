package kitchenpos.application;

import static kitchenpos.ProductFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("id가 없는 상품으로 id가 있는 상품을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final Product expectedProduct = createProductWithId(1L);
        given(productDao.save(any(Product.class))).willReturn(expectedProduct);

        assertThat(productService.create(createProductWithoutId())).isEqualToComparingFieldByField(expectedProduct);
    }

    @DisplayName("상품 가격이 null인 경우 예외를 반환한다.")
    @Test
    void createTest2() {
        final Product noPriceProduct = createProductWithPrice(null);

        assertThatThrownBy(() -> productService.create(noPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 음수인 경우 예외를 반환한다.")
    @Test
    void createTest3() {
        final Product minusPriceProduct = createProductWithPrice(BigDecimal.valueOf(-10000));

        assertThatThrownBy(() -> productService.create(minusPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<Product> expectedProducts = createProducts();
        given(productDao.findAll()).willReturn(expectedProducts);

        assertThat(productService.list()).usingRecursiveComparison()
            .isEqualTo(expectedProducts);
    }
}
