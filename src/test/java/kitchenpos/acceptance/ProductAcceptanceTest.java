package kitchenpos.acceptance;

import static kitchenpos.DomainFixtures.맛있는_라면;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 상품을_추가한다() {
        Product product = testRestTemplate.postForObject("http://localhost:" + port + "/api/products",
                맛있는_라면(), Product.class);

        assertThat(product.getName()).isEqualTo("맛있는 라면");
        assertThat(product.getId()).isNotZero();
    }

    @Test
    void 상품들을_조회한다() {
        testRestTemplate.postForObject("http://localhost:" + port + "/api/products",
                맛있는_라면(), Product.class);

        final List<Product> products = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/products", Product[].class));

        assertThat(products).hasSize(1);
    }
}
