package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductApiTest extends ApiTest {

    private static final String baseUrl = "/api/products";

    private List<Product> products;

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        products = new ArrayList<>();
        products.add(saveProduct("후라이드", 16000));
        products.add(saveProduct("양념치킨", 16000));
    }

    private Product saveProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return jdbcTemplateProductDao.save(product);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void postProduct() {
        Product request = new Product();
        request.setName("강정치킨");
        request.setPrice(BigDecimal.valueOf(17000));

        ResponseEntity<Product> responseEntity = testRestTemplate.postForEntity(baseUrl, request, Product.class);
        Product response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response).usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request);
    }

    @DisplayName("상품들을 조회할 수 있다.")
    @Test
    void getProducts() {
        ResponseEntity<Product[]> responseEntity = testRestTemplate.getForEntity(baseUrl, Product[].class);
        Product[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .hasSameSizeAs(products)
            .containsAll(products);
    }
}
