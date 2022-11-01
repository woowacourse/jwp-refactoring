package kitchenpos.domain;

import static kitchenpos.domain.DomainFixture.PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @DisplayName("상품을 생성할 때 ")
    @Nested
    class CreateProduct {

        @DisplayName("상품의 가격이 null 이면 생성할 수 없다.")
        @Test
        void createNullPrice() {
            assertThatThrownBy(() -> new Product(PRODUCT_NAME, null))
                    .isInstanceOf(InvalidProductException.class)
                    .hasMessage("상품 금액이 잘못됐습니다.");
        }

        @DisplayName("상품의 가격이 0 보다 작으면 생성할 수 없다.")
        @ParameterizedTest
        @ValueSource(longs = {-1, -10})
        void createInvalidPrice(long price) {
            assertThatThrownBy(() -> new Product(PRODUCT_NAME, BigDecimal.valueOf(price)))
                    .isInstanceOf(InvalidProductException.class)
                    .hasMessage("상품 금액이 잘못됐습니다.");
        }
    }

}