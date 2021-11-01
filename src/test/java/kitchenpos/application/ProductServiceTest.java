package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.generator.ProductGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ProductServiceTest extends ServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 등록")
    @ValueSource(ints = {0, 1, 17000})
    @ParameterizedTest
    void create(int price) {
        when(productDao.save(any(Product.class))).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        Product product = ProductGenerator.newInstance("강정치킨", price);
        Product actual = productService.create(product);

        verify(productDao, times(1)).save(product);
        assertThat(actual).isEqualTo(product);
    }

    @DisplayName("상품 등록시 가격이 0 미만이면 예외 처리")
    @ValueSource(ints = {-1, -2, -123456789})
    @ParameterizedTest
    void createWithInvalidPrice(int price) {
        Product product = ProductGenerator.newInstance("강정치킨", price);

        assertThatThrownBy(() -> productService.create(product)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
        verify(productDao, times(0)).save(any(Product.class));
    }

    @DisplayName("상품 조회")
    @Test
    void list() {
        List<Product> expected = Arrays.asList(new Product(), new Product(), new Product());
        when(productDao.findAll()).thenReturn(expected);

        List<Product> actual = productService.list();

        verify(productDao, times(1)).findAll();
        assertThat(actual).hasSameSizeAs(expected)
            .containsExactlyElementsOf(expected);
    }
}
