package kitchenpos.application;

import static kitchenpos.support.ProductFixtures.createAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("product 가격이 null이거나 0보다 작은 경우 예외가 발생한다.")
    @Test
    void create_ifPriceIsNullOrMinus_throwsException() {
        // given
        final Product product = new Product("후라이드", BigDecimal.valueOf(-1000));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product를 생성한다.")
    @Test
    void create() {
        // given
        final Product product = new Product("소금치킨", BigDecimal.valueOf(20000));

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("product들을 조회한다.")
    @Test
    void list() {
        // given
        final List<Product> expected = createAll();

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("price")
                .isEqualTo(expected);
    }
}
