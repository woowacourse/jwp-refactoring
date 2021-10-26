package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Product 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("Product 생성")
    @Test
    void create() {
        int price = 1000;
        Product product = new Product("product", BigDecimal.valueOf(price));
        Product created = makeResponse("/api/products", TestMethod.POST, product).as(Product.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo(product.getName()),
            () -> assertThat(created.getPrice().intValue()).isEqualTo(price)
        );
    }

    @DisplayName("Product 생성 실패 - price가 0보다 작다.")
    @Test
    void create_fail_price_type() {
        Product product = new Product("product", BigDecimal.valueOf(-500));
        assertThat(makeResponse("/api/products", TestMethod.POST, product).statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Product 리스트를 불러온다.")
    @Test
    void list() {
        Product product1 = new Product("product1", BigDecimal.valueOf(1000));
        Product product2 = new Product("product2", BigDecimal.valueOf(1000));
        makeResponse("/api/products", TestMethod.POST, product1);
        makeResponse("/api/products", TestMethod.POST, product2);

        List<Product> products = makeResponse("api/products", TestMethod.GET).jsonPath()
            .getList(".", Product.class);
        assertAll(
            () -> assertThat(products.size()).isEqualTo(2),
            () -> assertThat(products.stream()
                .map(Product::getName).collect(Collectors.toList()))
                .containsExactly(product1.getName(), product2.getName())
        );
    }
}