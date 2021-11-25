package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

import static org.assertj.core.api.Assertions.assertThat;

class TableServiceTest extends ServiceTest {

    private final OrderTableRequest emptyOrderTable;

    public TableServiceTest() {
        this.emptyOrderTable = Fixtures.makeOrderTable(true);
    }

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("테이블 생성")
    void createTest() {

        // when
        final OrderTable orderTable = tableService.create(emptyOrderTable);

        // then
        assertThat(tableService.list()).contains(orderTable);
    }

    @Test
    @DisplayName("현재 테이블의 빈테이블 상태를 다른 테이블의 빈테이블 상태로 변경")
    void changeEmptyTest() {

        // given
        final OrderTable fromOrderTable = tableService.create(emptyOrderTable);

        // when
        final OrderTable changeOrderTable = tableService.changeEmpty(fromOrderTable.getId(), false);

        // then
        assertThat(changeOrderTable.isEmpty()).isFalse();
    }
}
