package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.request.CreateProductRequest;
import kitchenpos.product.dto.response.ProductResponse;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 상품을 생성한다.")
        void create() {
            // given
            CreateProductRequest product = new CreateProductRequest("이름", new BigDecimal(1000));

            // when
            ProductResponse savedProduct = productService.create(product);

            // then
            assertThat(savedProduct.getId()).isNotNull();
        }

    }

    @Nested
    @DisplayName("list()")
    class ListMethod {

        @Test
        @DisplayName("전체 상품을 조회한다.")
        void list() {
            List<ProductResponse> products = productService.list();
            assertThat(products).isNotNull();
        }

    }

}
