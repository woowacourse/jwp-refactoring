package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        // given
        given(productDao.save(any())).willReturn(product);

        // when, then
        assertDoesNotThrow(() -> productService.create(product));
    }

    @Test
    @DisplayName("상품 생성 시 가격이 null이면 예외를 발생시킨다.")
    void throwExceptionWhenNullPrice() {
        // given
        product.setPrice(null);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> productService.create(product));
    }

    @Test
    @DisplayName("상품 생성 시 가격이 음수이면 예외를 발생시킨다.")
    void throwExceptionWhenNegativePrice() {
        // given
        product.setPrice(BigDecimal.valueOf(-1000));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> productService.create(product));
    }

    @Test
    @DisplayName("상품 목록을 반환한다.")
    void findAllProduct() {
        // given
        given(productDao.findAll()).willReturn(Collections.singletonList(product));

        // when, then
        assertDoesNotThrow(() -> productService.list());
    }





}