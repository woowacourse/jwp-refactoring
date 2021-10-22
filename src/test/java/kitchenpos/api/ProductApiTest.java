package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;
import kitchenpos.generator.ProductGenerator;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductApiTest extends ApiTest {

    private static final String BASE_URL = "/api/products";

    private List<Product> products;

    @Autowired
    private JdbcTemplateProductDao productDao;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        products = new ArrayList<>();
        products.add(productDao.save(ProductGenerator.newInstance("후라이드", 16000)));
        products.add(productDao.save(ProductGenerator.newInstance("양념치킨", 16000)));
    }

    @DisplayName("상품 등록")
    @Test
    void postProduct() {
        Product request = ProductGenerator.newInstance("강정치킨", 17000);

        ResponseEntity<Product> responseEntity = testRestTemplate.postForEntity(BASE_URL, request, Product.class);
        Product response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response).usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void getProducts() {
        ResponseEntity<Product[]> responseEntity = testRestTemplate.getForEntity(BASE_URL, Product[].class);
        Product[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .hasSameSizeAs(products)
            .containsAll(products);
    }
}
