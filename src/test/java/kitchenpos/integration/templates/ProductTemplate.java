package kitchenpos.integration.templates;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductTemplate {

    public static final String PRODUCT_URL = "/api/products";

    private final IntegrationTemplate integrationTemplate;

    public ProductTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

    public ResponseEntity<Product> create(String name, BigDecimal price) {
        ProductRequest productRequest = new ProductRequest(null, name, price);

        return integrationTemplate.post(
            PRODUCT_URL,
            productRequest,
            Product.class
        );
    }

    public ResponseEntity<ProductResponse[]> list() {
        return integrationTemplate.get(
            PRODUCT_URL,
            ProductResponse[].class
        );
    }
}
