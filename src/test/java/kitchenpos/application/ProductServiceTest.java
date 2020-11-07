package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("정상적으로 Product를 생성한다.")
    @Test
    void create() {
        Product productWithoutId = ProductFixture.createWithoutId();
        Product productWithId = ProductFixture.createWithId(1L);

        when(productDao.save(productWithoutId)).thenReturn(productWithId);

        Product savedProduct = productService.create(productWithoutId);
        assertThat(savedProduct)
            .isEqualToIgnoringGivenFields(productWithoutId, "id");
        assertThat(savedProduct).extracting(Product::getId)
            .isEqualTo(productWithId.getId());
    }

    @DisplayName("가격이 null인 Product 생성 요청시 예외를 반환한다.")
    @Test
    void createIllegalPriceProduct() {
        Product nullPriceProduct = ProductFixture.createNullPriceWithId(ProductFixture.ID2);

        assertThatThrownBy(() -> productService.create(nullPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수인 Product 생성 요청시 예외를 반환한다.")
    @Test
    void createNegativePriceProduct() {
        Product negativePriceProduct = ProductFixture.createNegativePriceWithId(ProductFixture.ID2);

        assertThatThrownBy(() -> productService.create(negativePriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Product를 불러온다.")
    @Test
    void list() {
        Product product1 = ProductFixture.createWithId(1L);
        Product product2 = ProductFixture.createWithId(2L);

        when(productDao.findAll()).thenReturn(Arrays.asList(product1, product2));

        assertThat(productService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(product1, product2));
    }
}
