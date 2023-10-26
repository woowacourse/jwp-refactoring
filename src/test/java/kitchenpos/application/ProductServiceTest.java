package kitchenpos.application;

import kitchenpos.product.controller.dto.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.InvalidProductPriceException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    void 상품을_생성할_때_상품의_가격이_0미만이면_예외가_발생한다() {
        // given
         ProductCreateRequest productCreateRequest = new ProductCreateRequest("짜장면", BigDecimal.valueOf(-1L));

        // when & then
        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(InvalidProductPriceException.class)
                .hasMessageContaining("상품 가격은 0원 이상이어야 합니다");
    }

    @Test
    void 상품을_생성한다() {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("짜장면", BigDecimal.valueOf(8000L));
        // when
        Product savedProduct = productService.create(productCreateRequest);

        // then
        assertThat(productRepository.findById(savedProduct.getId())).isPresent();
    }

    @Test
    void list() {
        // given
        Product product1 = PRODUCT("짜장면", 8000L);
        Product savedProduct1 = productRepository.save(product1);
        Product product2 = PRODUCT("딤섬", 8000L);
        Product savedProduct2 = productRepository.save(product2);

        // when
        List<Product> expected = List.of(savedProduct1, savedProduct2);
        List<Product> actual = productService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
