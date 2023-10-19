package kitchenpos.application;

import kitchenpos.EntityFactory;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductCreateRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private EntityFactory entityFactory;

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void create() {
        //given
        final ProductCreateRequest request = new ProductCreateRequest("떡볶이", BigDecimal.valueOf(5000));

        //when
        final ProductResponse product = productService.create(request);

        //then
        assertSoftly(softAssertions -> {
            assertThat(product.getId()).isNotNull();
            assertThat(product.getName()).isEqualTo("떡볶이");
        });
    }

    @Test
    @DisplayName("상품 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> productService.list());
    }

    @Test
    @DisplayName("ID로 조회할 때 존재하지 않는 상품이면 예외가 발생한다")
    void findById() {
        assertThatThrownBy(() -> productService.calculatePrice(0L, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("상품 가격을 계산할 수 있다")
    void calculatePrice() {
        //given
        final Product product = entityFactory.saveProduct("연어", 5000);

        //when
        final Price price = productService.calculatePrice(product.getId(), 10);

        //then
        assertThat(price.equalsWith(50000)).isTrue();
    }
}
