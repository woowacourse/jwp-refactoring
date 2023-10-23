package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.SingleOrderTableCreateRequest;
import kitchenpos.dto.table.TableGroupCreateRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable savedOrderTable1;

    private OrderTable savedOrderTable2;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = OrderTable.of(null, 10, true);
        OrderTable orderTable2 = OrderTable.of(null, 15, true);
        savedOrderTable1 = orderTableRepository.save(orderTable1);
        savedOrderTable2 = orderTableRepository.save(orderTable2);
    }

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 정상_요청() {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(savedOrderTable1.getId()),
                    SingleOrderTableCreateRequest.from(savedOrderTable2.getId())
            );

            // when
            TableGroupResponse response = tableGroupService.create(request);

            // then
            assertThat(response.getOrderTables())
                    .extracting(OrderTableResponse::getId)
                    .contains(savedOrderTable1.getId(), savedOrderTable2.getId());
        }

        @Test
        void 요청의_주문_테이블이_2개_미만이면_예외_발생() {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(savedOrderTable1.getId())
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청의_주문_테이블이_존재하지_않으면_예외_발생() {
            // given
            long invalidOrderTableId = -1L;
            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(invalidOrderTableId)
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 요청의_주문_테이블이_비어있지_않으면_예외_발생() {
            // given
            OrderTable orderTable = OrderTable.of(null, 15, false);
            orderTable = orderTableRepository.save(orderTable);
            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(orderTable.getId()),
                    SingleOrderTableCreateRequest.from(savedOrderTable1.getId())
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블에_그룹이_이미_존재하면_예외_발생() {
            // given
            TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(LocalDateTime.now()));
            OrderTable orderTable1 = OrderTable.of(savedTableGroup, 10, true);
            OrderTable orderTable2 = OrderTable.of(savedTableGroup, 15, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(savedOrderTable1.getId()),
                    SingleOrderTableCreateRequest.from(savedOrderTable2.getId())
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_삭제 {

        @Test
        void 정상_요청() {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(savedOrderTable1.getId()),
                    SingleOrderTableCreateRequest.from(savedOrderTable2.getId())
            );
            TableGroupResponse response = tableGroupService.create(request);

            // when, then
            assertDoesNotThrow(
                    () -> tableGroupService.ungroup(response.getId())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void 요청에_대한_주문의_상태가_COOKING이나_MEAL이면_예외_발생(OrderStatus orderStatus) {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    SingleOrderTableCreateRequest.from(savedOrderTable1.getId()),
                    SingleOrderTableCreateRequest.from(savedOrderTable2.getId())
            );
            TableGroupResponse response = tableGroupService.create(request);
            Order order = createOrder(savedOrderTable1, orderStatus);
            orderRepository.save(order);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.ungroup(response.getId())
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private TableGroupCreateRequest createTableGroup(final SingleOrderTableCreateRequest... tableRequests) {
        return TableGroupCreateRequest.from(Arrays.asList(tableRequests));
    }

    private Order createOrder(final OrderTable orderTable,
                              final OrderStatus status) {
        return Order.of(orderTable, status.name(), LocalDateTime.now());
    }
}
