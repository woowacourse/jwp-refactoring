package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.매튜_치킨_10000원;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static kitchenpos.fixture.ProductFixture.후추_칰힌_가격_책정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
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
        Product product = 후추_치킨_10000원();

        // when
        Long id = productService.create(product)
                .getId();

        // then
        Product savedProduct = productDao.findById(id)
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(product.getPrice()).isEqualByComparingTo(savedProduct.getPrice())
        );
    }

    @Test
    void price가_null_인_경우_상품_저장에_실패한다() {
        // given
        Product product = 후추_칰힌_가격_책정(null);

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_음수인_경우_상품_저장에_실패한다() {
        // given
        Product product = 후추_칰힌_가격_책정(BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_전체를_조회한다() {
        // given
        List<Product> products = List.of(
                후추_치킨_10000원(),
                매튜_치킨_10000원()
        );
        List<Product> savedProducts = new ArrayList<>();
        for (Product product : products) {
            savedProducts.add(productDao.save(product));
        }

        // when
        List<Product> results = productService.list()
                .stream()
                .filter(product ->
                        containsObjects(
                                savedProducts,
                                productInSavedProducts -> productInSavedProducts.getId().equals(product.getId())
                        )
                )
                .collect(Collectors.toList());

        // then
        assertThat(results).usingRecursiveComparison()
                .ignoringFields("id", "price")
                .isEqualTo(products);
    }

}
