package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductApiTest extends ApiTest {

    private static final String BASE_URL = "/api/products";

    @Autowired
    private ProductRepository productRepository;

    private List<Product> products;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        products = new ArrayList<>();
        products.add(productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000))));
        products.add(productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16000))));
    }

    @DisplayName("상품 등록")
    @Test
    void postProduct() {
        ProductRequest request = new ProductRequest("강정치킨", BigDecimal.valueOf(17000));

        ResponseEntity<ProductResponse> responseEntity = testRestTemplate.postForEntity(
            BASE_URL,
            request,
            ProductResponse.class
        );
        ProductResponse actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).usingComparatorForType(
                BigDecimalComparator.BIG_DECIMAL_COMPARATOR,
                BigDecimal.class
            ).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void getProducts() {
        ResponseEntity<ProductResponse[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL,
            ProductResponse[].class
        );
        ProductResponse[] actual = responseEntity.getBody();
        List<ProductResponse> expected = products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
            .usingComparatorForType(
                BigDecimalComparator.BIG_DECIMAL_COMPARATOR,
                BigDecimal.class
            ).hasSameSizeAs(expected)
            .containsAll(expected);
    }
}
