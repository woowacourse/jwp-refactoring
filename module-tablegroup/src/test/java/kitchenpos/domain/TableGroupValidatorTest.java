package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import org.junit.jupiter.api.Test;

class TableGroupValidatorTest {

    private final TableGroupValidator tableGroupValidator = new TableGroupValidator();

    @Test
    void 묶으려는_테이블이_저장되어_있지_않으면_예외_발생() {
        // given
        int requestSize = 2;
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, new NumberOfGuests(1), true)
        );

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupValidator.validate(requestSize, orderTables)
        );
    }

    @Test
    void 묶으려는_테이블은_2개_이상이어야_한다() {
        // given
        int requestSize = 1;
        List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, new NumberOfGuests(1), true)
        );

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupValidator.validate(requestSize, orderTables)
        );
    }
}
