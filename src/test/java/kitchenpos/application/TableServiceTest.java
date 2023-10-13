package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    void Order_Table_생성() {
        // given
        OrderTable orderTable = OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false);

        // when
        Long orderTableId = tableService.create(orderTable)
                .getId();

        // then
        OrderTable savedOrderTableId = orderTableDao.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(savedOrderTableId).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    void Order_Table_목록을_반환한다() {
        // given
        List<OrderTable> orderTables = List.of(
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false),
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false),
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false)
        );
        List<OrderTable> savedOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            savedOrderTables.add(tableService.create(orderTable));
        }

        // when
        List<OrderTable> orderTablesExcludeExistingData = tableService.list()
                .stream()
                .filter(orderTable ->
                        containsObjects(
                                savedOrderTables,
                                orderTableInSavedOrderTables -> orderTableInSavedOrderTables.getId()
                                        .equals(orderTable.getId())
                        )
                )
                .collect(Collectors.toList());

        // then
        assertThat(orderTablesExcludeExistingData).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTables);
    }

    @Test
    void 저장되어_있지_않은_OrderTable의_empty를_변경하는_경우_실패한다() {
        // given
        OrderTable orderTable = OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true);

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(MAX_VALUE, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable이_이미_TableGroup에_속해_있으면_empty_변경이_안된다() {
        // given
        TableGroup savedTableGroup = tableGroupDao.save(TableGroupFixture.빈_테이블_그룹_생성());
        OrderTable orderTable = OrderTableFixture.테이블_그룹이_있는_주문_테이블_생성(
                savedTableGroup,
                1,
                true
        );
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable emptyFalseOrderTable = OrderTableFixture.테이블_그룹이_있는_주문_테이블_생성(
                savedTableGroup,
                1,
                false
        );

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), emptyFalseOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable에_속해_있는_Order중_단_하나라도_요리중이면_안된다() {
        // given

        // when

        // then
    }

    @Test
    void OrderTable에_속해_있는_Order중_단_하나라도_식사중이면_안된다() {
        // given

        // when

        // then
    }

    @Test
    void OrderTable_empty를_성공적으로_변경한다() {
        // given

        // when

        // then
    }

    @Test
    void 존재하지_않는_OrderTable은_numberOfGuest를_변경할_수_없다() {
        // given

        // when

        // then
    }

    @Test
    void 주문이_불가능한_상태의_OrderTable_은_numberOfGuest_를_변경할_수_없다() {
        // given

        // when

        // then
    }

    @Test
    void OrderTable의_numberOfGuest를_0미만으로_바꿀_수_없다() {
        // given

        // when

        // then
    }

    @Test
    void OrderTable의_numberOfGuest를_성공적으로_변경한다() {
        // given

        // when

        // then
    }

}
