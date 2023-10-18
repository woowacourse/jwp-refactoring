package kitchenpos.application.integration;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest extends ApplicationIntegrationTest {

    @Test
    void create_product() {
        //given
        final String name = "후라이드";
        final BigDecimal price = BigDecimal.valueOf(1600000, 2);
        final Product product = new Product(name, price);

        //when
        final Product createdProduct = productService.create(product);

        //then
        assertThat(createdProduct)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(product);
    }

    @Test
    @Disabled
    void cannot_create_product_with_empty_name() {
        //given
        final String name = null;
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        final Product product = new Product(name, price);

        //when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_product_with_null_price() {
        //given
        final String name = "후라이드";
        final BigDecimal price = null;
        final Product product = new Product(name, price);

        //when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_product_with_negative_price() {
        //given
        final String name = "후라이드";
        final BigDecimal price = BigDecimal.valueOf(-16000.00);
        final Product product = new Product(name, price);

        //when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list_products() {
        //given
        final Product product1 = productService.create(new Product("후라이드", BigDecimal.valueOf(16000)));
        final Product product2 = productService.create(new Product("양념치킨", BigDecimal.valueOf(16000)));

        //when
        final Iterable<Product> products = productService.list();

        //then
        assertThat(products)
                .hasSize(2)
                .extracting("id")
                .containsExactlyInAnyOrder(product1.getId(), product2.getId());
    }
}