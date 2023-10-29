package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.application.event.dto.TableGroupCreateRequestEvent;
import kitchenpos.tablegroup.application.event.dto.TableGroupDeleteRequestEvent;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@RecordApplicationEvents
class TableGroupServiceTest extends ServiceTestConfig {

    @Autowired
    private ApplicationEvents applicationEvents;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(tableGroupRepository, eventPublisher);
    }

    @DisplayName("주문 테이블 그룹 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final List<Long> orderTableIdsInput = List.of(orderTable1.getId(), orderTable2.getId());
            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIdsInput);

            // when
            final TableGroupResponse actual = tableGroupService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getOrderTableIds().size()).isEqualTo(orderTableIdsInput.size());
                softly.assertThat(actual.getOrderTableIds()).isEqualTo(orderTableIdsInput);
            });
        }

        @DisplayName("OrderTables 가 비어있으면 실패한다.")
        @Test
        void fail_if_orderTables_are_empty() {
            // given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTables 에 1개의 원소만 들어있다면 실패한다.")
        @Test
        void fail_if_orderTables_size_is_one() {
            // given
            final OrderTable orderTable = saveEmptyOrderTable();
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable.getId()));

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("event가 하나 발행된다.")
        @Test
        void event() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final List<Long> orderTableIdsInput = List.of(orderTable1.getId(), orderTable2.getId());
            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIdsInput);

            // when
            tableGroupService.create(request);
            final long eventPublishedCount = applicationEvents.stream(TableGroupCreateRequestEvent.class).count();

            // then
            assertThat(eventPublishedCount).isOne();
        }
    }

    @DisplayName("주문 테이블 그룹 삭제")
    @Nested
    class UnGroup {
        @DisplayName("주문 테이블 그룹의 주문들이 모두 계산 상태라면 성공한다.")
        @Test
        void success_if_all_orders_orderStatus_are_COMPLETION() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final TableGroup tableGroup = saveTableGroup(List.of(orderTable1, orderTable2));

            final Order order1 = saveOrder(orderTable1);
            order1.changeStatus(OrderStatus.COMPLETION);
            final Order order2 = saveOrder(orderTable2);
            order2.changeStatus(OrderStatus.COMPLETION);

            // then
            assertThatCode(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .doesNotThrowAnyException();
        }

        @DisplayName("이벤트가 하나 발행된다.")
        @Test
        void event() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final TableGroup tableGroup = saveTableGroup(List.of(orderTable1, orderTable2));

            final Order order1 = saveOrder(orderTable1);
            order1.changeStatus(OrderStatus.COMPLETION);
            final Order order2 = saveOrder(orderTable2);
            order2.changeStatus(OrderStatus.COMPLETION);

            // when
            tableGroupService.ungroup(tableGroup.getId());

            final long eventPublishedCount = applicationEvents.stream(TableGroupDeleteRequestEvent.class).count();

            // then
            assertThat(eventPublishedCount).isOne();
        }
    }
}
