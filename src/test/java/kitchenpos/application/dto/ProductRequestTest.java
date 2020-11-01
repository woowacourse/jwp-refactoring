package kitchenpos.application.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductRequestTest extends DTOTest {
    @DisplayName("상품 요청 유효성 검사")
    @Test
    void validation() {
        ProductRequest request = new ProductRequest("강정치킨", BigDecimal.valueOf(17_000L));
        ProductRequest badRequest = new ProductRequest("", BigDecimal.valueOf(-1L));

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(2)
        );
    }
}