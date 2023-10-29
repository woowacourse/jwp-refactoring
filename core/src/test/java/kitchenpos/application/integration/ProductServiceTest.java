package kitchenpos.application.integration;

import common.domain.Money;
import common.domain.Name;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import product.dto.CreateProductRequest;
import product.dto.ListProductResponse;
import product.dto.ProductResponse;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest extends ApplicationIntegrationTest {

    @Test
    void create_product() {
        //given
        final String name = "후라이드";
        final long price = 16000L;
        final CreateProductRequest createProductRequest = CreateProductRequest.of(name, price);
        //when
        final ProductResponse createdProduct = productService.create(createProductRequest);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(createdProduct.getId()).isNotNull();
            softAssertions.assertThat(createdProduct.getName()).isEqualTo(name);
            softAssertions.assertThat(createdProduct.getPrice()).isEqualTo(price);
        });
    }

    @Test
    void cannot_create_product_with_empty_name() {
        //given
        final String name = null;
        final BigDecimal price = BigDecimal.valueOf(16000.00);
        CreateProductRequest createProductRequest = CreateProductRequest.of(name, price.longValue());

        //when & then
        assertThatThrownBy(() -> productService.create(createProductRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Name.NAME_IS_NOT_PROVIDED_ERROR_MESSAGE);
    }

    @Test
    void cannot_create_product_with_negative_price() {
        //given
        final String name = "후라이드";
        final BigDecimal price = BigDecimal.valueOf(-16000.00);
        CreateProductRequest createProductRequest = CreateProductRequest.of(name, price.longValue());

        //when & then
        assertThatThrownBy(() -> productService.create(createProductRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Money.AMOUNT_CANNOT_BE_BELOW_ZERO_ERROR_MESSAGE);
    }

    @Test
    void list_products() {
        //given
        final ProductResponse product1 = productService.create(CreateProductRequest.of("후라이드", BigDecimal.valueOf(16000).longValue()));
        final ProductResponse product2 = productService.create(CreateProductRequest.of("양념치킨", BigDecimal.valueOf(16000).longValue()));

        //when
        final ListProductResponse result = productService.list();

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.getProducts()).hasSize(2);
            softAssertions.assertThat(result.getProducts()).extracting("name").containsExactly(product1.getName(), product2.getName());
            softAssertions.assertThat(result.getProducts()).extracting("price").containsExactly(product1.getPrice(), product2.getPrice());
        });
    }
}