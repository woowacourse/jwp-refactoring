package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        OrderTable expected = new OrderTable();
        expected.setId(1L);
        expected.setTableGroupId(null);
        expected.setNumberOfGuests(0);
        expected.setEmpty(false);

        assertThat(actual).usingRecursiveComparison()
                 .isEqualTo(expected);
    }

    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        OrderTable orderTable1 = new OrderTable();

        OrderTable orderTable2 = new OrderTable();

        tableService.create(orderTable1);
        tableService.create(orderTable2);

        // when
        List<OrderTable> actual = tableService.list();
        orderTable1.setId(1L);
        orderTable2.setId(2L);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(Arrays.asList(orderTable1, orderTable2));
    }

    @ParameterizedTest
    @CsvSource(value = {"false", "false"})
    void 주문_테이블을_빈_상태로_변경한다(boolean status) {
        // given
        OrderTable savedOrderTable = tableService.create(new OrderTable());

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setEmpty(status);

        // when
        OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), changedOrderTable);

        // then
        assertThat(actual.isEmpty()).isEqualTo(status);
    }

    @Test
    void 존재하지_않는_주문_테이블의_상태를_변경할_수_없다() {
        // given
        long invalidOrderTable = 99999999L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTable, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Sql(scripts = {"/test_schema.sql", "/orderTable.sql"})
    void 이미_테이블_그룹에_속한_주문_테이블의_상태를_변경할_수_없다() {
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Sql(scripts = {"/test_schema.sql", "/orderTable.sql"})
    void 테이블이_주문목록에_존재하고_상태가_조리중이면_테이블의_상태를_변경할_수_없다() {
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Sql(scripts = {"/test_schema.sql", "/orderTable.sql"})
    void 테이블이_주문목록에_존재하고_식사중이면_테이블의_상태를_변경할_수_없다() {
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(3L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Sql(scripts = {"/test_schema.sql", "/orderTable.sql"})
    void 테이블이_주문목록에_존재하고_이미_계산이_완료되었으면_테이블의_상태를_변경할_수_있다() {
        // when & then
        assertThatCode(() -> tableService.changeEmpty(4L, new OrderTable()))
                .doesNotThrowAnyException();
    }

    @Test
    void 주문_테이블의_방문_손님의_수를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = tableService.create(orderTable);

        int numberOfGuests = 100;
        orderTable.setNumberOfGuests(numberOfGuests);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    void 주문_테이블의_방문_손님의_수를_0_미만으로_변경할_수_없다(int numberOfGuests) {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = tableService.create(orderTable);

        orderTable.setNumberOfGuests(numberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지않는_주문_테이블의_방문_손님의_수를_변경할_수_없다() {
        // given
        long invalidOrderTableId = 999999999L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태의_주문_테이블은_방문_손님의_수를_변경할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        OrderTable savedOrderTable = tableService.create(orderTable);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
