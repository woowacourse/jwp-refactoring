package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdDto;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class create는 {

        private final Long id = 1L;
        private final LocalDateTime createdDate = LocalDateTime.now();
        private final Long orderId = 11L;
        private final Long orderId2 = 12L;
        private final Long tableGroupId = null;
        private final int numberOfGuests = 3;
        private final boolean empty = true;
        private final OrderTable orderTable = new OrderTable(orderId, tableGroupId, numberOfGuests, empty);
        private final OrderTable orderTable2 = new OrderTable(orderId2, tableGroupId, numberOfGuests, empty);
        private final List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);

        @Test
        void order_table이_비어있으면_예외를_반환한다() {
            ArrayList<OrderTable> emptyOrderTable = new ArrayList<>();
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(emptyOrderTable);
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table의_크기가_2보다_작으면_예외를_반환한다() {
            // given
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(Arrays.asList(orderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_order_table이_비어있지_않으면_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(orderId, tableGroupId, numberOfGuests, false);
            List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(
                    orderTables);
            when(orderTableRepository.findAllByOrderTableIdsIn(any())).thenReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_order_table의_table_group_id가_null이_아니면_예외를_반환한다() {
            // given
            Long notNullTableGroupId = 111L;
            OrderTable orderTable = new OrderTable(orderId, notNullTableGroupId, numberOfGuests, empty);
            List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(orderTables);
            when(orderTableRepository.findAllByOrderTableIdsIn(any())).thenReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void table_group을_생성할_수_있다() {
            // given
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(orderTables);
            when(orderTableRepository.findAllByOrderTableIdsIn(any())).thenReturn(orderTables);

            when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(
                    request.toTableGroup());

            // when
            TableGroupResponse response = tableGroupService.create(request);

            // then
            assertThat(response.getCreatedDate()).isNotNull();
        }

        private TableGroupCreateRequest 테이블_그룹_생성_dto를_만든다(final List<OrderTable> orderTables) {
            return new TableGroupCreateRequest(orderTables.stream()
                    .map(OrderTableIdDto::new)
                    .collect(Collectors.toList()));
        }
    }

    @Nested
    class ungroup은 {

        private final Long orderId = 11L;
        private final Long orderId2 = 12L;
        private final Long tableGroupId = null;
        private final int numberOfGuests = 3;
        private final boolean empty = true;
        private final OrderTable orderTable1 = new OrderTable(orderId, tableGroupId, numberOfGuests, empty);
        private final OrderTable orderTable2 = new OrderTable(orderId2, tableGroupId, numberOfGuests, empty);

        @Test
        void order가_COOKING_또는_MEAL_상태이면_예외를_반환한다() {
            // given
            when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTable1, orderTable2));
            when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                    Arrays.asList(11L, 12L),
                    Arrays.asList(COOKING, MEAL))).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void table_group을_취소할_수_있다() {
            // given
            when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTable1, orderTable2));

            when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

            // when
            tableGroupService.ungroup(1L);

            // then
            Assertions.assertAll(
                    () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable1.isEmpty()).isFalse()
            );
        }
    }
}
