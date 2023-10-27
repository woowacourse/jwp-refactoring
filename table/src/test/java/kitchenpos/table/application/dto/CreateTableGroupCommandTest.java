package kitchenpos.table.application.dto;

import static kitchenpos.table.exception.TableGroupExceptionType.ORDER_TABLE_IDS_CAN_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class CreateTableGroupCommandTest {

    @Test
    void 주문_테이블_아이디들이_널이면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new CreateTableGroupCommand(null)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_IDS_CAN_NOT_NULL);
    }
}
