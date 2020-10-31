package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productDao);
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createTest() {
        when(productDao.save(any())).thenReturn(FRIED_CHICKEN);

        assertAll(
                () -> assertThat(productService.create(FRIED_CHICKEN).getId()).isEqualTo(1L),
                () -> assertThat(productService.create(FRIED_CHICKEN).getName()).isEqualTo("후라이드"),
                () -> assertThat(productService.create(FRIED_CHICKEN).getPrice()).isEqualTo(
                        BigDecimal.valueOf(15000))
        );
    }

    @DisplayName("상품가격이 0 원 이하일 경우 상품을 생성할 수 없다.")
    @Test
    void notCreateTest_underZeroPrice() {
        assertThatThrownBy(() -> productService.create(NEGATIVE_PRICE_PRODUCT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품가격이 null 일 경우 상품을 생성할 수 없다.")
    @Test
    void notCreateTest_priceNull() {
        assertThatThrownBy(() -> productService.create(NULL_PRICE_PRODUCT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품목록을 조회 할 수 있다.")
    @Test
    void listTest() {
        when(productDao.findAll()).thenReturn(PRODUCTS);

        assertAll(
                () -> assertThat(productService.list().size()).isEqualTo(2),
                () -> assertThat(productService.list().get(0).getName()).isEqualTo("후라이드"),
                () -> assertThat(productService.list().get(1).getName()).isEqualTo("콜라")
        );
    }
}