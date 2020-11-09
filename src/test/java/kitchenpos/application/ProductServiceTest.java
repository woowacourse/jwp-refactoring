package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.fixture.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest extends TestFixture {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 생성 예외 테스트: 가격이 음수일때")
    @Test
    void createFailByNegativePriceTest() {
        ProductCreateRequest negativeProductCreateRequest =
            new ProductCreateRequest(PRODUCT_NAME_1, -1L);

        assertThatThrownBy(() -> productService.create(negativeProductCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 성공 테스트")
    @Test
    void createTest() {
        ProductCreateRequest productCreateRequest =
            new ProductCreateRequest(PRODUCT_NAME_1, PRODUCT_PRICE_1.longValue());

        given(productDao.save(any())).willReturn(PRODUCT_1);

        Product persistedProduct = productService.create(productCreateRequest);

        assertThat(persistedProduct).usingRecursiveComparison().isEqualTo(PRODUCT_1);
    }

    @DisplayName("상품 조회 성공 테스트")
    @Test
    void listTest() {
        given(productDao.findAll()).willReturn(Arrays.asList(PRODUCT_1, PRODUCT_2));

        List<Product> persistedProducts = productService.list();

        assertAll(
            () -> assertThat(persistedProducts).hasSize(2),
            () -> assertThat(persistedProducts.get(0)).usingRecursiveComparison().isEqualTo(PRODUCT_1),
            () -> assertThat(persistedProducts.get(1)).usingRecursiveComparison().isEqualTo(PRODUCT_2)
        );
    }
}