package kitchenpos;

import kitchenpos.application.ProductService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 정상적으로 생성할 수 있다.")
    @Test
    void create() {
        // given
        Product product = new Product();
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(1000));

        given(productDao.save(product)).willReturn(product);

        // when
        Product actual = productService.create(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isNotNull();
            softly.assertThat(actual.getName()).isEqualTo(product.getName());
            softly.assertThat(actual.getPrice()).isEqualTo(product.getPrice());
        });
    }

    @DisplayName("상품 생성 시 상품 가격이 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = { -1L, -100L, -1000L })
    void create_FailWithNegativePrice(long invalidPrice) {
        // given
        Product invalidProduct = new Product();
        invalidProduct.setName("상품");
        invalidProduct.setPrice(BigDecimal.valueOf(invalidPrice));

        // when & then
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 시 상품 가격이 null인 경우 예외가 발생한다.")
    @Test
    void create_FailWithNullablePrice() {
        // given
        Product invalidProduct = new Product();
        invalidProduct.setName("상품");
        invalidProduct.setPrice(null);

        // when & then
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("")
    @Test
    void list() {
        // given
        Product 상품1 = new Product();
        상품1.setName("상품1");
        상품1.setPrice(BigDecimal.valueOf(1000));

        Product 상품2 = new Product();
        상품2.setName("상품1");
        상품2.setPrice(BigDecimal.valueOf(1000));

        given(productDao.findAll()).willReturn(List.of(상품1, 상품2));

        // when
        productService.list();
        
        // then
    }
}
