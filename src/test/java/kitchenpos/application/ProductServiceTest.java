package kitchenpos.application;

import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ProductCreateRequest request = new ProductCreateRequest("상품", 10000L);

        ProductResponse response = productService.create(request);

        Product findProduct = productDao.findById(response.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(findProduct.getName()).isEqualTo(request.getName());
        assertThat(findProduct.getPrice().longValue()).isEqualTo(request.getPrice());
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        Product product1 = new Product("상품1", 10000L);
        Product product2 = new Product("상품2", 15000L);
        Product product3 = new Product("상품3", 20000L);

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);

        List<ProductResponse> products = productService.list();

        assertThat(products).hasSize(3);
    }

    @AfterEach
    void tearDown() {
        productDao.deleteAll();
    }
}