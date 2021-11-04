package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceListTest extends TableServiceTest {

    @DisplayName("테이블을 조회한다.")
    @Test
    void getTables() {
        //given
        given(orderTableDao.findAll()).willReturn(standardTables);

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables.size()).isEqualTo(BASIC_TABLE_NUMBER);
    }

}
