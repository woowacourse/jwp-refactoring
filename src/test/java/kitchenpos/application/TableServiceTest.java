package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.exception.OrderExceptionType.ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION;
import static kitchenpos.exception.OrderTableExceptionType.ILLEGAL_CHANGE_NUMBER_OF_GUESTS;
import static kitchenpos.exception.OrderTableExceptionType.NOT_EXIST_ORDER_TABLE;
import static kitchenpos.exception.OrderTableExceptionType.NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION;
import static kitchenpos.exception.OrderTableExceptionType.TABLE_GROUP_IS_NOT_NULL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.annotation.IntegrationTest;
import kitchenpos.dao.jpa.JpaMenuRepository;
import kitchenpos.dao.jpa.JpaOrderRepository;
import kitchenpos.dao.jpa.JpaOrderTableRepository;
import kitchenpos.dao.jpa.JpaTableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderTableException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends IntegrationTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private JpaTableGroupRepository tableGroupRepository;
    @Autowired
    private JpaOrderTableRepository orderTableRepository;
    @Autowired
    private JpaOrderRepository orderRepository;
    @Autowired
    private JpaMenuRepository menuRepository;

    @Test
    void 테이블_저장() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(10, true);

        // when
        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        // then
        assertAll(
                () -> assertThat(orderTable.hasTableGroup()).isFalse(),
                () -> assertThat(orderTable.isEmpty()).isTrue(),
                () -> assertThat(orderTable.numberOfGuests()).isEqualTo(10)
        );
    }

    @Test
    void 모든_테이블_조회() {
        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).isNotNull();
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 현재_속한_TableGroup이_존재하면_예외를_발생한다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
            OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 1, false));
            Menu menu = menuRepository.getById(1L);
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(null, menu, new Quantity(1)),
                    new OrderLineItem(null, menu, new Quantity(2))
            );
            orderRepository.save(new Order(orderTable, MEAL, LocalDateTime.now(), orderLineItems));

            // when
            BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                    tableService.changeEmpty(orderTable.id(), false)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(TABLE_GROUP_IS_NOT_NULL_EXCEPTION);
        }

        @Test
        void 테이블의_Empty_상태를_변경할_때_테이블의_주문이_이미_계산_완료된_상태이면_예외가_발생한다() {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 10, false));
            Menu menu = menuRepository.getById(1L);
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(null, menu, new Quantity(1)),
                    new OrderLineItem(null, menu, new Quantity(2))
            );
            orderRepository.save(new Order(orderTable, COMPLETION, LocalDateTime.now(), orderLineItems));

            // when
            BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                    tableService.changeEmpty(orderTable.id(), true)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION);
        }

        @Test
        void 주문_테이블의_주문_가능_상태를_빈_테이블로_변경한다() {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 10, false));
            Menu menu = menuRepository.getById(1L);
            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(null, menu, new Quantity(1)),
                    new OrderLineItem(null, menu, new Quantity(2))
            );
            orderRepository.save(new Order(orderTable, COOKING, LocalDateTime.now(), orderLineItems));

            // when
            OrderTable result = tableService.changeEmpty(orderTable.id(), true);

            // then
            assertThat(result.isEmpty()).isTrue();
        }
    }

    @Nested
    class 테이블_손님_수_변경 {

        @ParameterizedTest
        @ValueSource(ints = {-1})
        @NullSource
        void 요청된_손님_수가_0_미만이면_예외가_발생한다(Integer numberOfGuests) {
            // given
            long orderTableId = 1L;

            // then
            BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                    tableService.changeNumberOfGuests(orderTableId, numberOfGuests)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION);
        }

        @Test
        void 요청된_주문_테이블이_DB에_저장되어_있지_않으면_예외를_발생한다() {
            // given
            long orderTableId = 124315434L;
            int numberOfGuests = 10;

            // then
            BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                    tableService.changeNumberOfGuests(orderTableId, numberOfGuests)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NOT_EXIST_ORDER_TABLE);
        }

        @Test
        void 변경하려는_주문_테이블이_빈_테이블이면_예외를_발생한다() {
            // given
            OrderTable saved = orderTableRepository.save(new OrderTable(null, 10, true));
            long orderTableId = saved.id();
            int numberOfGuests = 10;

            // then
            BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                    tableService.changeNumberOfGuests(orderTableId, numberOfGuests)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ILLEGAL_CHANGE_NUMBER_OF_GUESTS);
        }

        @Test
        void 테이블의_손님_수를_변경한다() {
            // given
            OrderTable saved = orderTableRepository.save(new OrderTable(null, 10, false));
            long orderTableId = saved.id();
            int numberOfGuests = 10;

            // then
            tableService.changeNumberOfGuests(orderTableId, numberOfGuests);

            // then
            assertThat(saved.numberOfGuests()).isEqualTo(10);
        }
    }
}
