package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    private OrderTableValidator orderTableValidator = new OrderTableValidator();

    @Test
    void 그룹화된_주문_테이블은_빈_테이블로_만들_수_없다() {
        // given
        OrderTable groupedOrderTable = new OrderTable(1L, 1L, 10, true);

        // when && then
        assertThatThrownBy(() -> orderTableValidator.changeEmpty(groupedOrderTable, true));
    }

}
