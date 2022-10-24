package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("create 메서드를 테스트한다")
    class create_test{

        @Test
        @DisplayName("정상적으로 Product를 생성한다.")
        void create(){
            final BigDecimal price = BigDecimal.valueOf(1000);
            final Product product = new Product("test product", price);

            final Product actual = productService.create(product);

            assertThat(actual).isNotNull();
        }

        @Test
        @DisplayName("가격이 null이면 예외를 발생시킨다.")
        void create_nullPrice() {
            final BigDecimal price = null;
            final Product product = new Product("test product", price);

            assertThatThrownBy(() ->  productService.create(product))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수이면 예외를 발생시킨다.")
        void create_negativePrice(){
            final BigDecimal price = BigDecimal.valueOf(-1000);
            final Product product = new Product("test product", price);

            assertThatThrownBy(() ->  productService.create(product))
                            .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    @DisplayName("현재 등록된 물품들을 반환한다.")
    void list(){
        final List<Product> actual = productService.list();
        assertThat(actual).hasSize(6);
    }


}
