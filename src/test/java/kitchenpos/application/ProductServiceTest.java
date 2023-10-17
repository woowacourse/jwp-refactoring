package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("상품 생성 테스트")
    class CreateTest {
        @Test
        @DisplayName("상품을 생성할 수 있다")
        void create() {
            //given
            final Product request = new Product();
            request.setName("떡볶이");
            request.setPrice(BigDecimal.valueOf(5000));

            //when
            final Product product = productService.create(request);

            //then
            assertSoftly(softAssertions -> {
                assertThat(product.getId()).isNotNull();
                assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(5000));
                assertThat(product.getName()).isEqualTo("떡볶이");
            });
        }

        @Test
        @DisplayName("상품을 생성할 때 가격이 음수면 예외가 발생한다")
        void create_fail() {
            //given
            final Product request = new Product();
            request.setName("떡볶이");
            request.setPrice(BigDecimal.valueOf(-1));

            //when
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 생성할 때 가격이 존재하지 않으면 예외가 발생한다")
        void create_fail2() {
            //given
            final Product request = new Product();
            request.setName("떡볶이");

            //when
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("상품 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> productService.list());
    }
}
