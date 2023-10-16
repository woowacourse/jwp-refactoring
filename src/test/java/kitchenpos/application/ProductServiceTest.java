package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.persistence.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_저장_성공() {
        // given
        Product expected = new Product("고추바사삭", 10000L);

        // when
        Product actual = productService.create(expected);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 상품_저장_실패_가격이_음수() {
        // given

        // when && then
        assertThatThrownBy(() -> productService.create(new Product("고추바사삭", -1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품_조회() {
        // given
        List<Product> expected = new ArrayList<>();
        expected.add(productRepository.save(new Product("고추바사삭", 10000L)));
        expected.add(productRepository.save(new Product("뿌링클", 20000L)));
        expected.add(productRepository.save(new Product("맛초킹", 3000L)));

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
