package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.OrderTable;

import static org.assertj.core.api.Assertions.assertThat;

class TableServiceTest extends ServiceTest {

    private final OrderTable emptyOrderTable;
    private final OrderTable nonEmptyOrderTable;

    public TableServiceTest() {
        this.emptyOrderTable = new OrderTable(1L, null, 1, true);
        this.nonEmptyOrderTable = new OrderTable(2L, null, 1, false);
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
        final OrderTable changeOrderTable = tableService.changeEmpty(fromOrderTable.getId(), nonEmptyOrderTable);

        // then
        assertThat(changeOrderTable.isEmpty()).isFalse();
    }
}
