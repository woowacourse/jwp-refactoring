package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을")
    @Nested
    class CreateTest {

        @DisplayName("생성한다.")
        @Test
        void create() {
            // given
            BigDecimal price = BigDecimal.valueOf(17000);
            ProductRequest productRequest = new ProductRequest("치킨", price);

            // when
            ProductResponse actual = productService.create(productRequest);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("치킨"),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(price)
            );
        }

        @DisplayName("마이너스 가격으로 생성할 수 없다.")
        @Test
        void minusPrice() {
            // given
            BigDecimal price = BigDecimal.valueOf(-100);
            ProductRequest productRequest = new ProductRequest("치킨", price);

            // when then
            assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0 이상이어야 합니다.");
        }
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertAll(
            () -> assertThat(actual).hasSize(6),
            () -> assertThat(actual)
                .extracting("name")
                .containsOnly("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨")
        );
    }
}
