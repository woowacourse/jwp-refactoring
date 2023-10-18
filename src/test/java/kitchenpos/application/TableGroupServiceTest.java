package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.exception.OrderTableExceptionType.ORDER_STATUS_IS_NOT_COMPLETION;
import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_SIZE_NOT_ENOUGH;
import static kitchenpos.exception.TableGroupExceptionType.ILLEGAL_ADD_ORDER_TABLE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.common.IntegrationTest;
import kitchenpos.dao.jpa.JpaOrderRepository;
import kitchenpos.dao.jpa.JpaOrderTableRepository;
import kitchenpos.dao.jpa.JpaTableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import org.junit.jupiter.api.Disabled;
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
    private JpaOrderRepository orderRepository;

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
            TableGroup savedTableGroup = tableGroupRepository.getReferenceById(1L);
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(savedTableGroup, 1, false));
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
    class 테이블_그룹_헤제 {

        // TODO: 전체 테스트로 하면 자꾸 실패.. 아마 아래 테스트로 데이터에 영향이 가는 듯하다. flyway를 사용하는 상황에서 어떻게 테스트 간의 데이터 독립성을 지킬 수 있을까?
        @Test
        @Disabled
        void 요청된_테이블_그룹_ID로_묶인_주문_테이블들의_상태가_하나라도_COOKING이거나_MEAL이면_예외를_발생한다() {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, true));

            TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(
                    new OrderTableIdRequest(1L),
                    new OrderTableIdRequest(2L),
                    new OrderTableIdRequest(orderTable.id())
            ));
            TableGroup tableGroup = tableGroupService.create(tableGroupCreateRequest);
            orderTable.changeEmpty(false);

            orderRepository.save(new Order(orderTable, COOKING, LocalDateTime.now(), List.of()));

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
