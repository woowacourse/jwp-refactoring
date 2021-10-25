package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

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

    @Test
    void create_fail_price_type() {
        Product product = new Product("product", BigDecimal.valueOf(-500));
        assertThat(makeResponse("/api/products", TestMethod.POST, product).statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

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