package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록한다")
    @Test
    void create() {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(2000L));

        given(productDao.save(any(Product.class))).willReturn(product);
        Product savedProduct = productService.create(product);

        assertAll(
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo("콜라"),
            () -> assertThat(savedProduct.getPrice().longValue()).isEqualTo(2000L)
        );
    }

    @DisplayName("상품의 가격이 음수나 Null인 경우 예외가 발생하는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"-1,"})
    void createInvalidProduct(Long price) {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(price));
        // todo csv 변경
        assertThatThrownBy(
            () -> productService.create(product)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void list() {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(2000L));

        given(productDao.findAll()).willReturn(Arrays.asList(product));
        List<Product> products = productService.list();

        assertAll(
            () -> assertThat(products).hasSize(1),
            () -> assertThat(products.get(0).getName()).isEqualTo("콜라"),
            () -> assertThat(products.get(0).getPrice().longValue()).isEqualTo(2000L)
        );
    }
}
