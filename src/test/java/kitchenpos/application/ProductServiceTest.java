package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Product;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록하고, 조회한다")
    @TestFactory
    Stream<DynamicTest> productServiceTest() {
        return Stream.of(
            dynamicTest("상품이 생성되는지 확인한다.", this::create),
            dynamicTest("상품에 가격이 음수나 Null인 경우 예외가 발생하는지 확인한다.", this::createInvalidProduct),
            dynamicTest("상품 리스트를 조회한다.", this::list)
        );
    }

    void create() {
        Product product = new Product();
        product.setName("콜라");
        product.setPrice(BigDecimal.valueOf(2000L));

        Product savedProduct = productService.create(product);

        assertAll(
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo("콜라"),
            () -> assertThat(savedProduct.getPrice().longValue()).isEqualTo(2000L)
        );
    }

    private void createInvalidProduct() {
        Product cola = new Product();
        cola.setName("콜라");
        cola.setPrice(null);

        Product chicken = new Product();
        chicken.setName("콜라");
        chicken.setPrice(BigDecimal.valueOf(-1));

        assertAll(
            () -> assertThatThrownBy(
                () -> productService.create(cola)
            ).isInstanceOf(IllegalArgumentException.class),

            () -> assertThatThrownBy(
                () -> productService.create(chicken)
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    void list() {
        List<Product> products = productService.list();

        assertAll(
            () -> assertThat(products).hasSize(1),
            () -> assertThat(products.get(0).getName()).isEqualTo("콜라"),
            () -> assertThat(products.get(0).getPrice().longValue()).isEqualTo(2000L)
        );
    }
}