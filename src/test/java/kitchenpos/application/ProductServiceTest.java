package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 저장한다.")
    @Test
    void saveProduct() {
        Product product = givenProduct("강정치킨", 17000);

        Product savedProduct = productService.create(product);

        List<Product> products = productService.list();
        assertThat(products).extracting(Product::getId, Product::getName, p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(savedProduct.getId(), "강정치킨", 17000)
                );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        Product product1 = givenProduct("강정치킨", 17000);
        Product product2 = givenProduct("마늘치킨", 18000);
        Product product3 = givenProduct("간장치킨", 19000);

        Product savedProduct1 = productService.create(product1);
        Product savedProduct2 = productService.create(product2);
        Product savedProduct3 = productService.create(product3);

        List<Product> products = productService.list();
        assertThat(products).extracting(Product::getId, Product::getName, p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(savedProduct1.getId(), "강정치킨", 17000),
                        tuple(savedProduct2.getId(), "마늘치킨", 18000),
                        tuple(savedProduct3.getId(), "간장치킨", 19000)
                );
    }

    private Product givenProduct(String name, long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
