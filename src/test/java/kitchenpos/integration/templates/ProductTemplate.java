package kitchenpos.integration.templates;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.factory.ProductFactory;
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
        Product product = ProductFactory.builder()
            .name(name)
            .price(price)
            .build();

        return integrationTemplate.post(
            PRODUCT_URL,
            product,
            Product.class
        );
    }

    public ResponseEntity<Product[]> list() {
        return integrationTemplate.get(
            PRODUCT_URL,
            Product[].class
        );
    }
}
