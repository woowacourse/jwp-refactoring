package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductQuantityTest {

    @DisplayName("생성자 테스트 - IAE 발생, Quantity가 1보다 작은 경우")
    @ParameterizedTest
    @ValueSource(longs = {0, -1, -2})
    void constructor_QuantityLessMoreOne_ThrownIllegalArgumentException(long quantity) {
        assertThatThrownBy(() -> new MenuProductQuantity(quantity))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - 성공, Quantity가 1보다 크거나 같은 경우")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void constructor_QuantityMoreThanOrEqualOne_Success(long quantity) {
        MenuProductQuantity menuProductQuantity = new MenuProductQuantity(quantity);

        assertThat(menuProductQuantity.getQuantity()).isEqualTo(quantity);
    }
}
