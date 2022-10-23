package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Product;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("가격이 null이라면 예외를 발생시킨다.")
    void createwithNullPriceError() {
        //given
        Product product = new Product();
        product.setName("test");

        //when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수라면 예외를 발생시킨다.")
    void createwithNeagativePriceError() {
        //given
        Product product = new Product();
        product.setName("test");
        product.setPrice(BigDecimal.valueOf(-1));

        //when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 상품 목록을 조회한다.")
    void findByList() {
        assertThat(productService.list()).hasSize(6);
    }
}