package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ProductsTest {

    @DisplayName("id로 product 추출하기")
    @Test
    void findById() {
        Products products = new Products(new ArrayList<>(Arrays.asList(
                new Product(1L, "상품1", new Money(1000L)),
                new Product(2L, "상품2", new Money(2000L)))
        ));

        Product actual = products.findById(1L);

        assertAll(
                () -> assertEquals(actual.getId(), 1L),
                () -> assertEquals(actual.getName(), "상품1"),
                () -> assertEquals(actual.getPrice(), new Money(1000L))
        );
    }
}