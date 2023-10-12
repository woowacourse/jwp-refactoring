package kitchenpos.application;

import static kitchenpos.fixture.ProductFixtures.후라이드치킨_16000원;
import static kitchenpos.fixture.ProductFixtures.후라이드치킨_16000원_ID1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
        // given
        Product product = 후라이드치킨_16000원;
        Product expected = 후라이드치킨_16000원_ID1;

        given(productDao.save(product)).willReturn(expected);

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("가격이 0원보다 낮은 상품을 생성하면 예외가 발생한다")
    @ValueSource(ints = {-1, -100, -35_000, -100_000})
    @ParameterizedTest
    void create_PriceLowerThanZero_ExceptionThrown(int invalidPrice) {
        // given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(invalidPrice));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
