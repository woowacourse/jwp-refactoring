package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TableGroupTest {

    @DisplayName("여러 개의 테이블을 단체로 지정한다.")
    @Test
    void ofUnsaved() {
        final OrderTable orderTable = mock(OrderTable.class);
        final List<OrderTable> twoTables = Arrays.asList(orderTable, orderTable);
        final TableGroup tableGroup = TableGroup.ofUnsaved(twoTables);

        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("2개 미만의 테이블을 단체로 지정하면 예외가 발생한다.")
    @ValueSource(ints = {0, 1})
    @ParameterizedTest(name = "{0}개의 테이블을 단체로 지정하면 예외가 발생한다.")
    void ofUnsaved_throwsException_ifSizeUnder2(final int sizeOfTables) {
        final List<OrderTable> tables = mock(List.class);
        when(tables.size()).thenReturn(sizeOfTables);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TableGroup.ofUnsaved(tables));
    }
}
