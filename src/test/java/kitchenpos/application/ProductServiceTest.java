package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.application.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class ProductServiceTest {

    private final ProductService productService;

    @Autowired
    ProductServiceTest(final ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("product를 생성한다.")
    @Test
    void product를_생성한다() {
        Product 후라이드 = 후라이드();

        Product actual = productService.create(후라이드);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo(후라이드.getName());
            assertThat(actual.getPrice().compareTo(후라이드.getPrice())).isEqualTo(0);
        });
    }

    @DisplayName("price가 null인 경우 예외를 던진다.")
    @Test
    void price가_null인_경우_예외를_던진다() {
        Product 후라이드 = generateProduct("후라이드", null);

        assertThatThrownBy(() -> productService.create(후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void price가_0미만인_경우_예외를_던진다(final int price) {
        Product 후라이드 = generateProduct("후라이드", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product list를 조회한다.")
    @Test
    void product_list를_조회한다() {
        Product 후라이드 = productService.create(후라이드());
        Product 양념치킨 = productService.create(양념치킨());

        List<String> actual = productService.list()
                .stream()
                .map(Product::getName)
                .collect(toList());

        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual)
                    .containsExactly(후라이드.getName(), 양념치킨.getName());
        });
    }
}
