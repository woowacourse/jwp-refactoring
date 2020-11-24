package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.exception.InvalidProductPriceException;
import kitchenpos.product.service.ProductService;

@SpringBootTest
@Sql(value = "/truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록한다")
    @Test
    void create() {
        ProductCreateRequest request = new ProductCreateRequest("콜라", 2000L);
        Long id = productService.create(request);

        assertThat(id).isNotNull();
    }

    @DisplayName("상품의 가격이 음수나 Null인 경우 예외가 발생하는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"-1,"})
    void createInvalidProduct(Long price) {
        ProductCreateRequest request = new ProductCreateRequest("콜라", price);
        assertThatThrownBy(
            () -> productService.create(request)
        ).isInstanceOf(InvalidProductPriceException.class);
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void list() {
        ProductCreateRequest request = new ProductCreateRequest("콜라", 2000L);
        productService.create(request);
        List<ProductResponse> products = productService.list().getProductResponses();

        assertAll(
            () -> assertThat(products).hasSize(1),
            () -> assertThat(products.get(0).getName()).isEqualTo("콜라"),
            () -> assertThat(products.get(0).getPrice().longValue()).isEqualTo(2000L)
        );
    }
}
