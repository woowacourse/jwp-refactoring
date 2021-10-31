package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Table 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;

    private static Stream<Arguments> create() {
        return Stream.of(
                Arguments.of(OrderTableFixture.FIRST.getTableGroupId(), true),
                Arguments.of(OrderTableFixture.FIRST.getTableGroupId(), false),
                Arguments.of(OrderTableFixture.SECOND.getTableGroupId(), true),
                Arguments.of(null, true)
        );
    }

    @DisplayName("주문테이블 저장 - 성공")
    @CustomParameterizedTest
    @MethodSource
    void create(@AggregateWith(OrderTableAggregator.class) OrderTable orderTable) {
        //given
        //when
        final OrderTable actual = tableService.create(orderTable);
        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
    }

    @DisplayName("주문테이블 조회 - 성공 - 전체 주문테이블 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<OrderTable> actual = tableService.list();
        //then
        assertThat(actual).extracting(OrderTable::getId)
                .containsAnyElementsOf(OrderTableFixture.orderTablesId());
    }

    @DisplayName("주문테이블 empty 값 수정 - 성공")
    @Test
    void changeEmpty() {
        //given
        //when
        final OrderTable actual = tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_TRUE);
        //then
        assertThat(actual.getId()).isEqualTo(OrderTableFixture.FIRST.getId());
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블 empty 값 수정 - 실패 - 주문테이블이 존재하지 않음")
    @Test
    void changeEmptyWhenNotFoundOrderTable() {
        //given
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, OrderTableFixture.TO_EMPTY_FALSE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 empty 값 수정 - 실패 - 주문테이블이 단체에 지정되어 있음")
    @Test
    void changeEmptyWhenOrderTableAssignTableGroup() {
        //given
        tableGroupService.create(TableGroupFixture.FIRST_SECOND_TABLE);
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 empty 값 수정 - 실패 - 주문테이블에 할당된 주문의 상태가 COMPLETITON이 아님")
    @Test
    void changeEmptyWhenOrderTableOrderIsNotCOMPLETION() {
        //given
        //when
        tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_FALSE);
        orderService.create(OrderFixture.FIRST_TABLE_후라이드치킨_하나);
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 성공")
    @Test
    void changeNumberOfGuests() {
        //given
        //when
        tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_FALSE);
        final OrderTable actual = tableService.changeNumberOfGuests(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_NUMBER_OF_GUESTS_FIVE);
        //then
        assertThat(actual.getNumberOfGuests()).isEqualTo(OrderTableFixture.TO_NUMBER_OF_GUESTS_FIVE.getNumberOfGuests());
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 실패 - 주문테이블이 존재하지 않음")
    @Test
    void changeNumberOfGuestsFailureWhenNotFoundOrderTable() {
        //given
        //when
        tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_FALSE);
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, OrderTableFixture.TO_EMPTY_FALSE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 실패 - 손님의 수가 0 미만")
    @CustomParameterizedTest
    @ValueSource(ints = {-1, -9999})
    void changeNumberOfGuestsFailureWhenNumberOfGuestLowerThanZero(int numberOfGuests) {
        //given
        final OrderTable expect = new OrderTable();
        expect.setNumberOfGuests(numberOfGuests);
        //when
        tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_FALSE);
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(OrderTableFixture.FIRST.getId(), expect))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 실패 - 테이블의 empty값이 true")
    @Test
    void changeNumberOfGuestsFailureWhenTableEmptyTrue() {
        //given
        //when
        tableService.changeEmpty(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_EMPTY_TRUE);
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(OrderTableFixture.FIRST.getId(), OrderTableFixture.TO_NUMBER_OF_GUESTS_FIVE))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    private static class OrderTableAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            final OrderTable orderTable = new OrderTable();
            orderTable.setTableGroupId(accessor.getLong(0));
            orderTable.setEmpty(accessor.getBoolean(1));
            return orderTable;
        }
    }
}