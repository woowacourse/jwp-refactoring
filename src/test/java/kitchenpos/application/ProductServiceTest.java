package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @DisplayName("새로운 상품을 생성한다.")
    @Test
    void create() {
        Product product
            = TestObjectFactory.createProduct("핫후라이드", new BigDecimal(18_000L));
        Product savedProductService = productService.create(product);

        assertAll(() -> {
            assertThat(savedProductService).isInstanceOf(Product.class);
            assertThat(savedProductService.getId()).isNotNull();
            assertThat(savedProductService.getName()).isNotNull();
            assertThat(savedProductService.getName()).isEqualTo("핫후라이드");
            assertThat(savedProductService.getPrice()).isNotNull();
            assertThat(savedProductService.getPrice().toBigInteger())
                .isEqualTo(new BigDecimal(18_000L).toBigInteger());
        });
    }

    @DisplayName("새로운 상품을 생성한다. - 메뉴 가격이 null일 경우")
    @Test
    void create_IfPriceIsNull_ThrowException() {
        Product product
            = TestObjectFactory.createProduct("핫후라이드", null);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 상품을 생성한다. - 메뉴 가격이 0 이하일 경우")
    @Test
    void createIfPriceIsNotPositive_ThrowException() {
        Product product
            = TestObjectFactory.createProduct("핫후라이드", new BigDecimal(-18_000L));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 상품을 조회할 수 있다.")
    @Test
    void list() {
        List<Product> products = productService.list();

        assertAll(() -> {
            assertThat(products).isNotEmpty();
            assertThat(products).hasSize(6);
        });
    }
}
