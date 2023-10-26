package kitchenpos.application;

import static kitchenpos.support.fixture.domain.ProductFixture.getProduct;
import static kitchenpos.support.fixture.dto.ProductCreateRequestFixture.productCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.product.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 상품을_추가할_때 {

        @Test
        void 가격이_0원_미만일_떄_예외를_던진다() {
            //given
            final ProductCreateRequest product = productCreateRequest("product", -1L);

            //when
            //then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 추가에_성공한다() {
            //given
            final ProductCreateRequest product = productCreateRequest("product", 0L);

            //when
            final Product savedProduct = productService.create(product);

            //then
            assertThat(productRepository.findById(savedProduct.getId())).isPresent();
        }
    }

    @Test
    void 상품_목록을_조회한다() {
        //given
        final Product pizza = productRepository.save(getProduct("product1", 10L));
        final Product chicken = productRepository.save(getProduct("product2", 10L));

        //when
        final List<Product> products = productService.list();

        //then
        assertThat(products)
                .usingRecursiveComparison()
                .isEqualTo(List.of(pizza, chicken));
    }
}
