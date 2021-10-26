package kitchenpos.acceptance.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 등록 성공")
    @Test
    void create() {
        Product chicken = new Product();
        chicken.setName("강정치킨");
        chicken.setPrice(BigDecimal.valueOf(17000));

        ResponseEntity<Product> responseEntity = testRestTemplate.postForEntity(
                "/api/products",
                chicken,
                Product.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("상품 등록 실패 - 가격 부재")
    @Test
    void createByNullPrice() {
        Product chicken = new Product();
        chicken.setName("강정치킨");

        ResponseEntity<Product> responseEntity = testRestTemplate.postForEntity(
                "/api/products",
                chicken,
                Product.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 등록 실패 - 가격 0 이하")
    @Test
    void createByNegativePrice() {
        Product chicken = new Product();
        chicken.setName("강정치킨");
        chicken.setPrice(BigDecimal.valueOf(-1));

        ResponseEntity<Product> responseEntity = testRestTemplate.postForEntity(
                "/api/products",
                chicken,
                Product.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        Product chicken1 = new Product();
        chicken1.setName("강정치킨");
        chicken1.setPrice(BigDecimal.valueOf(17000));
        Product chicken2 = new Product();
        chicken2.setName("간장치킨");
        chicken2.setPrice(BigDecimal.valueOf(17000));

        productDao.save(chicken1);
        productDao.save(chicken2);

        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity(
                "/api/products",
                List.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }
}
