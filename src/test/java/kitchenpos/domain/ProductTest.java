package kitchenpos.domain;

import static kitchenpos.DomainFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("상품의 가격이 0원 미만이면 예외가 발생한다.")
    void product() {
        // when & then
        assertThatThrownBy(() -> createProduct("김피탕", -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
