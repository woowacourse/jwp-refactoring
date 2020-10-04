package kitchenpos.application;

import kitchenpos.TestObjectFactory;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("Product 생성 메서드 테스트")
    @ParameterizedTest
    @CsvSource({"강정치킨, 17000", "양념치킨, 0"})
    void create(String name, int price) {
        Product product = TestObjectFactory.createProduct(name, price);

        Product savedProduct = productService.create(product);
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(name),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(String.valueOf(price))
        );
    }

    @DisplayName("Product 생성 - price 값이 null일 때 예외 처리")
    @Test
    void createWhenIllegalPrice() {
        String name = "강정치킨";
        Product product = new Product();
        product.setName(name);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 생성 - price 값이 0보다 작을 때 예외 처리")
    @Test
    void createWhenIllegalPrice2() {
        String name = "강정치킨";
        Product product = TestObjectFactory.createProduct(name, -1);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void name() {
        productService.create(TestObjectFactory.createProduct("강정치킨", 17000));
        productService.create(TestObjectFactory.createProduct("앙념치킨", 17000));

        List<Product> list = productService.list();

        assertThat(list).hasSize(2);
    }
}
