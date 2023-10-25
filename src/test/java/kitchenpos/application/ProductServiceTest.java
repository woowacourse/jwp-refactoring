package kitchenpos.application;

import kitchenpos.product.ProductDao;
import kitchenpos.product.Product;
import kitchenpos.product.ProductDto;
import kitchenpos.product.ProductService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductDao productDao;

    @Test
    void 상품을_생성한다() {
        // given
        ProductDto product = new ProductDto(1L, "product", BigDecimal.valueOf(1000));

        given(productDao.save(any()))
                .willReturn(product.toDomain());

        // when
        ProductDto result = productService.create(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(product.getId());
            softly.assertThat(result.getName()).isEqualTo(product.getName());
            softly.assertThat(result.getPrice()).isEqualTo(product.getPrice());
        });
    }

    @Test
    void 가격이_0원_보다_작은_상품을_생성하면_예외를_던진다() {
        // given
        ProductDto product = new ProductDto("product", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품을_전체_조회한다() {
        // given
        Product product1 = new Product(1L, "product1", BigDecimal.valueOf(1000));
        Product product2 = new Product(1L, "product2", BigDecimal.valueOf(2000));

        List<Product> expected = List.of(product1, product2);

        given(productDao.findAll())
                .willReturn(expected);

        // when
        List<ProductDto> result = productService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
