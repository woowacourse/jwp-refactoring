package kitchenpos.service;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Product 테스트")
class ProductServiceTest {

    private static final BigDecimal INVALID_PRICE = BigDecimal.valueOf(-1);

    @Autowired
    private ProductService productService;

    @DisplayName("Product 추가 테스트 - 성공")
    @Test
    void create() {
        //given
        Product product = ProductFixture.create();
        //when
        Product create = productService.create(product);
        //then
        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("Product 추가 테스트 - 실패 - 가격이 0보다 작은 경우")
    @Test
    void createFailureWhenInvalidPrice() {
        //given
        Product product = ProductFixture.create(1L, "INVALID", INVALID_PRICE);
        Product nullProduct = ProductFixture.create(1L, "INVALID", null);
        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(nullProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 Product 반환")
    @Test
    void list() {
        //given
        Product product = ProductFixture.create();
        productService.create(product);
        //when
        //when
        List<Product> products = productService.list();
        //then
        assertThat(products).isNotEmpty();
    }
}
