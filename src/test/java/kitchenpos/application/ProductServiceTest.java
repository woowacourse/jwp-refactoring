package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

    private Product product1;
    private Product product2;
    private Product nullPriceProduct;
    private Product negativePriceProduct;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);

        product1 = ProductFixture.createWithoutId(ProductFixture.PRICE1);
        product2 = ProductFixture.createWithId(ProductFixture.ID1, ProductFixture.PRICE1);
        nullPriceProduct = ProductFixture.createWithId(ProductFixture.ID2, null);
        negativePriceProduct = ProductFixture.createWithId(ProductFixture.ID2, -1000L);
    }

    @DisplayName("정상적으로 Product를 생성한다.")
    @Test
    void create() {
        when(productDao.save(product1)).thenReturn(product2);

        assertThat(productService.create(product1))
            .isEqualToComparingFieldByField(product2);
    }

    @DisplayName("가격이 null이거나 음수인 Product를 생성요청하면 예외를 반환한다.")
    @Test
    void createIllegalPriceProduct() {
        assertThatThrownBy(()->productService.create(nullPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(()->productService.create(negativePriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Product를 불러온다.")
    @Test
    void list() {
        when(productDao.findAll()).thenReturn(Arrays.asList(product1, product2));

        assertThat(productService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(product1, product2));
    }
}
