package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.매튜_치킨_10000원;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void 정상적으로_상품을_생성한다() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                "후추 치킨",
                10000L
        );

        // when
        Long id = productService.create(request)
                .getId();

        // then
        Product actual = productRepository.findById(id).get();
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("후추 치킨"),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000L))
        );
    }

    @Test
    void 상품_전체를_조회한다() {
        // given
        List<Product> products = List.of(
                후추_치킨_10000원(),
                매튜_치킨_10000원()
        );
        List<Product> expected = new ArrayList<>();
        for (Product product : products) {
            expected.add(productRepository.save(product));
        }

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "price")
                .isEqualTo(expected);
    }

}
