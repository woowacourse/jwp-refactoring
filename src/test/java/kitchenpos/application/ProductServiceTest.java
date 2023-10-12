package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.*;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

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
    @DisplayName("price 가 음수인 경우 저장에 실패한다.")
    void price가_음수인_경우_상품_저장에_실패한다() {
        // given
        Product product = 후추_칰힌_가격_책정(BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Product 전체를 조회한다.")
    void 상품_전체를_조회한다() {
        // given
        List<Product> products = List.of(
                후추_치킨_10000원(),
                매튜_치킨_10000원(),
                루카_치킨_10000원()
        );
        List<Product> savedProducts = new ArrayList<>();
        for (Product product : products) {
            savedProducts.add(productDao.save(product));
        }

        // when
        List<Product> results = productService.list()
                .stream()
                .filter(product -> containsProducts(savedProducts, product))
                .collect(Collectors.toList());

        // then
        assertThat(results).usingRecursiveComparison()
                .ignoringFields("id", "price")
                .isEqualTo(products);
    }

    private boolean containsProducts(List<Product> products, Product product) {
        for (Product productInProducts : products) {
            if (productInProducts.getId().equals(product.getId())) {
                return true;
            }
        }

        return false;
    }

}
