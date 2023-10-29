package ordertable.test.java.kitchenpos.product.application;

import static kitchenpos.fixture.ProductFixture.CHICKEN;
import static kitchenpos.fixture.ProductFixture.CHICKEN_NON_ID;
import static kitchenpos.fixture.ProductFixture.CHICKEN_REQUEST;
import static kitchenpos.fixture.ProductFixture.COKE_NON_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void create_메서드는_상품을_생성한다() {
        // when
        final Product createdProduct = productService.create(CHICKEN_REQUEST);

        // then
        assertThat(createdProduct)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .usingRecursiveComparison()
                .isEqualTo(CHICKEN);
    }

    @Test
    void list_메서드는_모든_상품을_조회한다() {
        // given
        final Product chicken = productRepository.save(CHICKEN_NON_ID);
        final Product coke = productRepository.save(COKE_NON_ID);

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .usingRecursiveComparison()
                .isEqualTo(List.of(chicken, coke));
    }
}
