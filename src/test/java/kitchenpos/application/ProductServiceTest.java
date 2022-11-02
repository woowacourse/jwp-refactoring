package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends ApplicationTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 저장한다.")
    @Test
    void saveProduct() {
        ProductRequest productRequest = new ProductRequest("강정치킨", BigDecimal.valueOf(17_000));

        Long productId = productService.create(productRequest);

        List<ProductResponse> products = productService.list();
        assertThat(products).extracting(ProductResponse::getId, ProductResponse::getName,
                        p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(productId, "강정치킨", 17000)
                );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        ProductRequest productRequest1 = new ProductRequest("강정치킨", BigDecimal.valueOf(17_000));
        ProductRequest productRequest2 = new ProductRequest("마늘치킨", BigDecimal.valueOf(18_000));
        ProductRequest productRequest3 = new ProductRequest("간장치킨", BigDecimal.valueOf(19_000));

        Long productId1 = productService.create(productRequest1);
        Long productId2 = productService.create(productRequest2);
        Long productId3 = productService.create(productRequest3);

        List<ProductResponse> products = productService.list();
        assertThat(products).extracting(ProductResponse::getId, ProductResponse::getName,
                        p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(productId1, "강정치킨", 17000),
                        tuple(productId2, "마늘치킨", 18000),
                        tuple(productId3, "간장치킨", 19000)
                );
    }
}
