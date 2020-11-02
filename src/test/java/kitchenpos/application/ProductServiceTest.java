package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createTest() {
        Product createProduct = TestObjectUtils.createProduct(null, "후라이드",
                BigDecimal.valueOf(16000));
        when(productDao.save(any())).thenReturn(FRIED_CHICKEN);

        Product product = productService.create(createProduct);
        assertAll(
                () -> assertThat(product.getId()).isEqualTo(1L),
                () -> assertThat(product.getName()).isEqualTo("후라이드"),
                () -> assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(16000))
        );
    }

    @DisplayName("상품가격이 0 원 이하일 경우 상품을 생성할 수 없다.")
    @Test
    void notCreateTest_when_underZeroPrice() {
        assertThatThrownBy(() -> productService.create(NEGATIVE_PRICE_PRODUCT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품가격이 null 일 경우 상품을 생성할 수 없다.")
    @Test
    void notCreateTest_when_priceNull() {
        assertThatThrownBy(() -> productService.create(NULL_PRICE_PRODUCT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품목록을 조회 할 수 있다.")
    @Test
    void listTest() {
        when(productDao.findAll()).thenReturn(PRODUCTS);

        List<Product> list = productService.list();
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(list.get(0).getName()).isEqualTo("후라이드"),
                () -> assertThat(list.get(1).getName()).isEqualTo("양념치킨")
        );
    }
}