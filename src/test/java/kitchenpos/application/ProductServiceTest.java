package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

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
        Product product = TestObjectUtils.createProduct(1L, "후라이드",
                BigDecimal.valueOf(15000));

        when(productDao.save(any())).thenReturn(product);

        assertAll(
                () -> assertThat(productService.create(product).getId()).isEqualTo(1L),
                () -> assertThat(productService.create(product).getName()).isEqualTo("후라이드"),
                () -> assertThat(productService.create(product).getPrice()).isEqualTo(
                        BigDecimal.valueOf(15000))
        );
    }

    @DisplayName("상품가격이 0 원 이하일 경우 상품을 생성할 수 없다.")
    @Test
    void notCreateTest_underZeroPrice() {
        Product requestProduct = TestObjectUtils.createProduct(1L, "후라이드",
                BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(requestProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품가격이 null 일 경우 상품을 생성할 수 없다.")
    @Test
    void notCreateTest_priceNull() {
        Product requestProduct = TestObjectUtils.createProduct(1L, "후라이드",
                null);

        assertThatThrownBy(() -> productService.create(requestProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품목록을 조회 할 수 있다.")
    @Test
    void listTest() {
        Product chicken = TestObjectUtils.createProduct(1L, "후라이드",
                BigDecimal.valueOf(150000));
        Product cola = TestObjectUtils.createProduct(2L, "콜라",
                BigDecimal.valueOf(1000));

        List<Product> products = Arrays.asList(chicken, cola);

        when(productDao.findAll()).thenReturn(products);

        assertAll(
                () -> assertThat(productService.list().size()).isEqualTo(2),
                () -> assertThat(productService.list().get(0).getName()).isEqualTo("후라이드"),
                () -> assertThat(productService.list().get(1).getName()).isEqualTo("콜라")
        );
    }
}