package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.utils.KitchenPosClassCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class ProductServiceTest {
    public static final String TEST_PRODUCT_NAME_1 = "후라이드 치킨";
    public static final String TEST_PRODUCT_NAME_2 = "코카콜라";
    public static final BigDecimal TEST_PRODUCT_PRICE_1 = new BigDecimal("15000.00");
    public static final BigDecimal TEST_PRODUCT_PRICE_2 = new BigDecimal("1000.00");

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = KitchenPosClassCreator.createProduct(TEST_PRODUCT_NAME_1, TEST_PRODUCT_PRICE_1);
    }


    @DisplayName("Product 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        Product savedProduct = productService.create(product);

        assertEquals(savedProduct.getName(), product.getName());
        assertEquals(savedProduct.getPrice(), product.getPrice());
    }

    @DisplayName("예외 테스트 : Product 생성 중 0 미만의 가격이 전달될 경우, 올바르게 수행된다.")
    @ValueSource(longs = {-1000000, -42, -1})
    @ParameterizedTest
    void createNegativePriceExceptionTest(long source) {
        BigDecimal invalidPrice = BigDecimal.valueOf(source);
        product.setPrice(invalidPrice);
        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Product 생성 중 가격이 null일 경우, 올바르게 수행된다.")
    @Test
    void createNullPriceExceptionTest() {
        product.setPrice(null);
        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 전체 목록을 조회 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        Product secondProduct = KitchenPosClassCreator.createProduct(TEST_PRODUCT_NAME_2, TEST_PRODUCT_PRICE_2);
        product = productService.create(product);
        secondProduct = productService.create(secondProduct);

        List<Product> foundProducts = productService.list();

        assertThat(foundProducts)
                .hasSize(2)
                .extracting("id")
                .containsOnly(product.getId(), secondProduct.getId());
    }
}
