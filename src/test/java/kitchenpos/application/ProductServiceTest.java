package kitchenpos.application;

import kitchenpos.TestObjectFactory;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("Product 생성 메서드 테스트")
    @Test
    void create() {
        String name = "강정치킨";
        int price = 17000;
        Product product = TestObjectFactory.createProduct(name, price);

        Product savedProduct = productService.create(product);
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(name),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(String.valueOf(price))
        );
    }
}
