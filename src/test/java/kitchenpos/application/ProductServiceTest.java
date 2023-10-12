package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void beforeEach() {
        product = new Product();
        product.setPrice(new BigDecimal(1000));
        product.setId(1L);
        product.setName("product");
    }

    @DisplayName("product를 생성한다.")
    @Test
    void product_생성() {
        //given
        final Product expected = product;
        given(productDao.save(expected))
                .willReturn(expected);

        //when
        final Product actual = productService.create(expected);

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual).isEqualTo(expected);
        });
    }

    @DisplayName("price가 null이면 생성을 실패한다.")
    @Test
    void price가_null이면_예외() {
        //given
        final Product expected = product;
        product.setPrice(null);

        // when & then
        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price가 음수면 생성을 실패한다.")
    @Test
    void price가_음수면_예외() {
        //given
        final Product expected = product;
        product.setPrice(new BigDecimal(-100));

        // when & then
        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product를 전체 조회한다.")
    @Test
    void product_전체_조회() {
        //given
        given(productDao.findAll())
                .willReturn(List.of(product));

        //when
        final List<Product> actual = productService.list();

        assertSoftly(softly -> {
            softly.assertThat(actual.size()).isEqualTo(1);
            softly.assertThat(actual.get(0)).isEqualTo(product);
        });
    }
}
