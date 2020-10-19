package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create: 이름과 가격을 입력 받아, 제품을 생성 요청 시, 입력 값을 기반으로  ID와 제품이 생성된다.")
    @Test
    void create() {
        final Product product = new Product();
        product.setName("맛난 치킨");
        product.setPrice(BigDecimal.valueOf(16_000));
        final Product product1 = productService.create(product);
        assertAll(
                () -> assertThat(product1.getId()).isNotNull(),
                () -> assertThat(product1.getName()).isEqualTo("맛난 치킨"),
                () -> assertThat(product1.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16_000))
        );
    }

    @DisplayName("create: 제품 생성 요청시, 입력 받은 가격이 음수라면, 제품 생성 시 예외가 발생한다.")
    @Test
    void create_throw_exception_if_price_is_negative() {
        final Product product = new Product();
        product.setName("맛난 치킨");
        product.setPrice(BigDecimal.valueOf(-16_000));
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("create: 제품 생성 요청시, 입력 받은 가격이 null이라면, 제품 생성 시 예외가 발생한다.")
    @Test
    void create_throw_exception_if_price_is_null() {
        final Product product = new Product();
        product.setName("맛난 치킨");
        product.setPrice(null);
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("list: 현재 저장 되어 있는 상품의 목록을 반환한다.")
    @Test
    void list() {
        final List<Product> list = productService.list();

        assertThat(list).hasSize(6);
    }
}