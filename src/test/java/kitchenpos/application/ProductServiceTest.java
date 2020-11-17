package kitchenpos.application;

import static kitchenpos.helper.ProductHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Product;

@SpringBootTest
@Sql("/truncate.sql")
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @ValueSource(longs = {0L, 100_000_000_000L})
    @ParameterizedTest
    void create(long price) {
        // given
        String 상품명 = "상품명";
        BigDecimal 가격 = BigDecimal.valueOf(price);
        Product 추가할_상품 = createProduct(상품명, 가격);

        // when
        Product 상품 = productService.create(추가할_상품);

        // then
        assertAll(
                () -> assertThat(상품.getId()).isNotNull(),
                () -> assertThat(상품.getName()).isEqualTo(상품명),
                () -> assertThat(상품.getPrice().longValueExact()).isEqualTo(가격.longValueExact())
        );
    }

    @DisplayName("상품의 가격이 0원보다 작은 경우 예외가 발생한다.")
    @Test
    void create_PriceIsUnderZero_ExceptionThrown() {
        // given
        String 상품명 = "상품명";
        BigDecimal 가격 = BigDecimal.valueOf(-1);
        Product 추가할_상품 = createProduct(상품명, 가격);

        // when
        // then
        assertThatThrownBy(() -> productService.create(추가할_상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        Product 상품_1 = productService.create(createProduct("상품1", BigDecimal.valueOf(1_000L)));
        Product 상품_2 = productService.create(createProduct("상품2", BigDecimal.valueOf(1_000L)));

        // when
        List<Product> 상품목록 = productService.list();

        // then
        assertAll(
                () -> assertThat(상품목록.get(0).getName()).isEqualTo(상품_1.getName()),
                () -> assertThat(상품목록.get(0).getPrice()).isEqualTo(상품_1.getPrice()),
                () -> assertThat(상품목록.get(1).getName()).isEqualTo(상품_2.getName()),
                () -> assertThat(상품목록.get(1).getPrice()).isEqualTo(상품_2.getPrice())
        );
    }
}
