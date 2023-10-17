package kitchenpos.domain;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.TableGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("tableGroup을 생성할 때 orderTable의 사이즈가 2보다 작거나, 비어있으면 예외처리한다.")
    void orderTablesSizeIsNotValidated() {
        assertThatThrownBy(() -> new TableGroup(now(), emptyList()))
            .isInstanceOf(TableGroupException.class)
            .hasMessage(ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY.getMessage());
    }
}
