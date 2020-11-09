package kitchenpos.domain;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductsTest {
    @DisplayName("Products를 정상 생성한다.")
    @Test
    void createTest() {
        Product product = createProduct(1L, "상풍", 1000);
        assertDoesNotThrow(() -> new Products(Collections.singletonList(product)));
    }

    @DisplayName("Products 생성시 list가 비어있는 경우 예외를 반환한다.")
    @Test
    void createTest2() {
        assertThatThrownBy(() -> new Products(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
