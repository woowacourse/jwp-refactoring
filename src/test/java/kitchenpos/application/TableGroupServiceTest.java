package kitchenpos.application;

import static kitchenpos.exception.OrderTableExceptionType.ORDER_STATUS_IS_NOT_COMPLETION;
import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_SIZE_NOT_ENOUGH;
import static kitchenpos.exception.TableGroupExceptionType.ILLEGAL_ADD_ORDER_TABLE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.common.IntegrationTest;
import kitchenpos.dao.jpa.JpaOrderTableRepository;
import kitchenpos.dao.jpa.JpaTableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.CreateOrderLineItemRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private JpaOrderTableRepository orderTableRepository;
    @Autowired
    private JpaTableGroupRepository tableGroupRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 테이블_그룹_저장 {

        @ParameterizedTest
        @MethodSource("illegalOrderTableIds")
        void 요청된_테이블의_수가_2개_미만이면_예외를_발생한다(List<OrderTableIdRequest> orderTableIds) {
            // given
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

            // when
            BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                    tableGroupService.create(tableGroupCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_TABLE_SIZE_NOT_ENOUGH);
        }

        Stream<Arguments> illegalOrderTableIds() {
            return Stream.of(
                    Arguments.of(List.of()),
                    Arguments.of(List.of(new OrderTableIdRequest(1L)))
            );
        }

        @Test
        void 요청된_모든_테이블들이_빈_테이블이_아니면_예외를_발생한다() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, false));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 1, true));
            List<OrderTableIdRequest> orderTableIds = List.of(
                    new OrderTableIdRequest(orderTable1.id()),
                    new OrderTableIdRequest(orderTable2.id())
            );
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

            // when
            BaseExceptionType exceptionType = assertThrows(TableGroupException.class, () ->
                    tableGroupService.create(tableGroupCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ILLEGAL_ADD_ORDER_TABLE_EXCEPTION);
        }

        @Test
        void 요청된_모든_테이블들이_이미_다른_테이블_그룹이_저장되어_있으면_예외를_발생한다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(tableGroup, 1, false));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 1, false));
            List<OrderTableIdRequest> orderTableIds = List.of(
                    new OrderTableIdRequest(orderTable1.id()),
                    new OrderTableIdRequest(orderTable2.id())
            );
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

            // when
            BaseExceptionType exceptionType = assertThrows(TableGroupException.class, () ->
                    tableGroupService.create(tableGroupCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ILLEGAL_ADD_ORDER_TABLE_EXCEPTION);
        }
    }

    @Nested
    class 테이블_그룹_해제 {

        @Test
        void 요청된_테이블_그룹_ID로_묶인_주문_테이블들의_상태가_하나라도_COOKING이거나_MEAL이면_예외를_발생한다() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, false));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, false));

            Order order1 = orderService.create(new OrderCreateRequest(
                    orderTable1.id(), List.of(
                    new CreateOrderLineItemRequest(1L, 1L),
                    new CreateOrderLineItemRequest(1L, 2L))
            ));
            Order order2 = orderService.create(new OrderCreateRequest(
                    orderTable2.id(), List.of(
                    new CreateOrderLineItemRequest(1L, 1L),
                    new CreateOrderLineItemRequest(1L, 2L))
            ));

            tableService.changeEmpty(orderTable1.id(), true);
            tableService.changeEmpty(orderTable2.id(), true);

            TableGroup tableGroup = tableGroupService.create(new TableGroupCreateRequest(List.of(
                    new OrderTableIdRequest(orderTable1.id()),
                    new OrderTableIdRequest(orderTable2.id())
            )));

            order1.changeOrderStatus(OrderStatus.COOKING);

            // when
            BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                    tableGroupService.ungroup(tableGroup.id())
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(ORDER_STATUS_IS_NOT_COMPLETION);
        }

        @Test
        void 요청된_테이블_그룹_ID로_묶인_주문_테이블들을_빈_테이블로_만들어준다() {
            // given
            List<OrderTableIdRequest> orderTableIdRequests = List.of(
                    new OrderTableIdRequest(1L),
                    new OrderTableIdRequest(2L)
            );
            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIdRequests);

            // when
            tableGroupService.create(tableGroupCreateRequest);

            // then
            OrderTable orderTable1 = orderTableRepository.getById(1L);
            OrderTable orderTable2 = orderTableRepository.getById(2L);

            assertThat(orderTable1.isEmpty()).isTrue();
            assertThat(orderTable2.isEmpty()).isTrue();
        }
    }
}
