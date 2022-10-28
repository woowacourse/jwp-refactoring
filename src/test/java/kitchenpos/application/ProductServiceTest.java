package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        //given
        ProductRequest productRequest = new ProductRequest("test", 100);

        //when, then
        assertDoesNotThrow(() -> productService.create(productRequest));
    }

    @Test
    @DisplayName("가격이 음수라면 예외를 발생시킨다.")
    void createWithNegativePriceError() {
        //given
        ProductRequest productRequest = new ProductRequest("test", -1);

        //when, then
        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 상품 목록을 조회한다.")
    void findByList() {
        //given
        List<Product> products = productService.list();

        //when
        ProductRequest productRequest = new ProductRequest("test", 10);
        productService.create(productRequest);

        //then
        assertThat(productService.list()).hasSize(products.size() + 1);
    }
}
