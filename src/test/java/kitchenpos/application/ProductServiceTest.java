package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static kitchenpos.fixture.ProductFixture.createProductWithPrice;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("Product 생성 테스트")
    @Test
    void create() {
        Product product = createProductWithPrice(BigDecimal.ONE);
        given(productDao.save(product)).willReturn(createProduct(1L, BigDecimal.ONE));

        Product actual = productService.create(product);
        Product expect = createProduct(1L, BigDecimal.ONE);

        assertThat(actual).isEqualToComparingFieldByField(expect);
    }

    @DisplayName("price가 0보다 작을 경우 예외 테스트")
    @Test
    void createPriceLessThanZero() {
        Product product = createProductWithPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product 전체 조회")
    @Test
    void list() {
        given(productDao.findAll()).willReturn(Arrays.asList(createProduct(1L, BigDecimal.ONE)));

        assertThat(productService.list()).hasSize(1);
    }
}