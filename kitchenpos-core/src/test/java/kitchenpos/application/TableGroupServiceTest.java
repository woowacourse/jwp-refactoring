package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.dto.ordertable.request.OrderTableCreateRequest;
import kitchenpos.dto.tablegroup.request.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {
    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    TableService tableService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("테이블 그룹을 생성할 때, ")
    class CreateTableGroup {

        @Test
        @DisplayName("정상적으로 생성한다")
        void createTableGroup() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final OrderTableCreateRequest orderTableCreateRequestB = new OrderTableCreateRequest(2, true);
            final Long orderTableIdB = tableService.create(orderTableCreateRequestB);
            final TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(orderTableIdA, orderTableIdB));

            // when
            final Long orderTableGroupId = tableGroupService.create(tableGroupCreateRequest);

            // then
            assertThat(orderTableGroupId).isPositive();
        }

        @Test
        @DisplayName("주문 테이블 목록이 비어있을 시 예외 발생")
        void orderTablesEmptyException() {
            // given
            final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                    Collections.emptyList());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
        }

        @Test
        @DisplayName("주문 테이블이 2개 미만일 시 예외 발생")
        void orderTablesLessThanTwoException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(orderTableIdA));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
        }

        @Test
        @DisplayName("주문 테이블 개수와 실제 저장되어있던 주문 테이블 개수가 불일치 할 시 예외 발생")
        void orderTablesCountWrongException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(orderTableIdA, -1L));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
        }

        @Test
        @DisplayName("비어있지 않은 주문 테이블이 한 개라도 존재할 시 예외 발생")
        void notEmptyOrderTableExistException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final OrderTableCreateRequest orderTableCreateRequestB = new OrderTableCreateRequest(2, false);
            final Long orderTableIdB = tableService.create(orderTableCreateRequestB);
            final TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(orderTableIdA, orderTableIdB));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
        }

        @Test
        @DisplayName("TableGroupId가 null이 아닌 주문 테이블이 한 개라도 존재할 시 예외 발생")
        void tableGroupOfNotNullTableGroupIdExistException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final OrderTableCreateRequest orderTableCreateRequestB = new OrderTableCreateRequest(2, true);
            final Long orderTableIdB = tableService.create(orderTableCreateRequestB);

            final OrderTableCreateRequest orderTableCreateRequestC = new OrderTableCreateRequest(3, true);
            final Long orderTableIdC = tableService.create(orderTableCreateRequestC);
            final TableGroupCreateRequest tableGroupCreateRequestB =
                    new TableGroupCreateRequest(List.of(orderTableIdB, orderTableIdC));
            tableGroupService.create(tableGroupCreateRequestB);

            final TableGroupCreateRequest tableGroupCreateRequestA =
                    new TableGroupCreateRequest(List.of(orderTableIdA, orderTableIdB, orderTableIdC));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroupCreateRequestA));
        }
    }

    @Nested
    @DisplayName("테이블 그룹 제거 시, ")
    class DeleteTableGroup {
        @Test
        @DisplayName("정상적으로 제거한다")
        void deleteTableGroup() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final OrderTableCreateRequest orderTableCreateRequestB = new OrderTableCreateRequest(2, true);
            final Long orderTableIdB = tableService.create(orderTableCreateRequestB);
            final TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(orderTableIdA, orderTableIdB));

            final Long tableGroupId = tableGroupService.create(tableGroupCreateRequest);

            // when, then
            assertThatNoException()
                    .isThrownBy(() -> tableGroupService.ungroup(tableGroupId));
        }

        @Test
        @DisplayName("테이블 그룹에 속한 주문 테이블 중, 요리 또는 식사중인 테이블이 하나라도 존재할 시 예외 발생")
        void notCompletionOrderStatusExistException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(orderTableCreateRequestA);
            final OrderTableCreateRequest orderTableCreateRequestB = new OrderTableCreateRequest(2, true);
            final Long orderTableIdB = tableService.create(orderTableCreateRequestB);
            final TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(orderTableIdA, orderTableIdB));

            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTableIdA,
                    List.of(orderLineItemCreateRequest)
            );

            final Long tableGroupId = tableGroupService.create(tableGroupCreateRequest);
            orderService.create(orderCreateRequest);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.ungroup(tableGroupId));
        }
    }
}
