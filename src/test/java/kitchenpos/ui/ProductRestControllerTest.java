package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductRestControllerTest extends ControllerTest {

    @Autowired
    private ProductRestController productController;

    @Test
    void create() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(18000));
        ResponseEntity<Product> response = productController.create(product);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void createPriceLessThanZero() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(-1));
        assertThatThrownBy(() -> productController.create(product))
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void createPriceNull() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(null);
        assertThatThrownBy(() -> productController.create(product))
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void list() {
        createProduct("릭냥이네 치킨", 30000);
        createProduct("호호네 치킨", 30000);
        createProduct("제이슨네 치킨", 30000);

        ResponseEntity<List<Product>> response = productController.list();
        List<Product> products = response.getBody();

        assertThat(products).hasSize(3);
    }
}
