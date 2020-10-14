package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 추가")
    @Test
    void create() {
        ProductRequest request = createProductRequest(18_000);

        ProductResponse savedProduct = productService.create(request);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("전체 상품 조회")
    @Test
    void list() {
        ProductRequest request1 = createProductRequest(18_000);
        ProductRequest request2 = createProductRequest(20_000);

        productService.create(request1);
        productService.create(request2);

        List<ProductResponse> list = productService.list();

        assertThat(list).hasSize(2);
    }

    private ProductRequest createProductRequest(int price) {
        return ProductRequest.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .build();
    }
}