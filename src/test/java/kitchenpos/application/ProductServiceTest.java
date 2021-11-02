package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_PLACEHOLDER;
import static org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품 기능에서")
public class ProductServiceTest {

    private ProductFixture productFixture;
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productFixture = ProductFixture.createFixture();
        productDao = productFixture.getTestProductDao();
        productService = new ProductService(productDao);
    }

    @Nested
    @DisplayName("상품을 생성할 때")
    class CreateTest {

        @DisplayName("성공한다.")
        @Test
        void createTest() {
            //given
            String name = "새로운 상품";
            BigDecimal price = new BigDecimal("15000");

            //when
            Product product = new Product();
            product.setName("새로운 상품");
            product.setPrice(price);
            Product persistedProduct = productService.create(product);

            //then
            assertAll(
                () -> assertThat(persistedProduct.getName()).isEqualTo(name),
                () -> assertThat(persistedProduct.getPrice()).isEqualTo(price)
            );
        }

        @ParameterizedTest(name = ARGUMENTS_PLACEHOLDER + DISPLAY_NAME_PLACEHOLDER)
        @DisplayName("원(0원 미만)이면 실패한다.")
        @ValueSource(ints = {-1, -1000, -10000})
        @NullSource
        void whenSmallerThanZeroPriceOrNull(Integer amount) {
            //given
            String name = "새로운 상품";
            BigDecimal price = Objects.isNull(amount) ? null : new BigDecimal(amount);
            //when
            Product product = new Product();
            product.setName("새로운 상품");
            product.setPrice(price);

            //then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 목록을 받아온다.")
    @Test
    void listTest() {
        //given
        List<Product> expectedFixtures = ProductFixture.createFixture().getFixtures();
        //when
        List<Product> list = productService.list();
        //then
        assertAll(
            () -> assertThat(list.size()).isEqualTo(expectedFixtures.size()),
            () -> expectedFixtures.forEach(
                product -> assertThat(list).usingRecursiveFieldByFieldElementComparator()
                    .contains(product)
            )
        );
    }
}
