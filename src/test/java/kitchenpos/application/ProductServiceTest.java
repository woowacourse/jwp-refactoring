package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 생성")
    void create() {
        Product product = createProduct(null, null, 1000);
        given(productDao.save(any(Product.class))).willReturn(product);

        Product saved = productService.create(product);

        assertThat(saved).isNotNull();
        assertThat(saved.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품 목록 조회")
    void list() {
        Product product = createProduct(null, null, 1000);
        given(productDao.findAll()).willReturn(Collections.singletonList(product));

        List<Product> result = productService.list();

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).containsExactly(product)
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1000})
    @DisplayName("상품의 가격이 null이거나 0미만인 경우 예외 발생")
    void priceBelowZero(Integer input) {
        Product product = createProduct(null, null, input);
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
