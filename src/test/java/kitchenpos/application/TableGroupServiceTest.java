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
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

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
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(id, createdDate, new ArrayList<>());
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table의_크기가_2보다_작으면_예외를_반환한다() {
            // given
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(id, createdDate, Arrays.asList(orderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table의_크기가_찾은_order_table의_크기와_일치하지_않으면_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(orderId, tableGroupId, numberOfGuests, empty);
            when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable));

            OrderTable orderTable2 = new OrderTable(orderId, tableGroupId, numberOfGuests, empty);
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(id, createdDate, orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_order_table이_비어있지_않으면_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(orderId, tableGroupId, numberOfGuests, false);
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(id, createdDate,
                    Arrays.asList(orderTable, orderTable2));
            when(orderTableDao.findAllByIdIn(any())).thenReturn(request.getOrderTables());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_order_table의_table_group_id가_null이_아니면_예외를_반환한다() {
            // given
            Long notNullTableGroupId = 111L;
            OrderTable orderTable = new OrderTable(orderId, notNullTableGroupId, numberOfGuests, empty);
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(id, createdDate,
                    Arrays.asList(orderTable, orderTable2));
            when(orderTableDao.findAllByIdIn(any())).thenReturn(request.getOrderTables());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void table_group을_생성할_수_있다() {
            // given
            TableGroupCreateRequest request = 테이블_그룹_생성_dto를_만든다(id, createdDate, orderTables);
            when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);

            when(tableGroupDao.save(any(TableGroup.class))).thenReturn(request.toTableGroup(LocalDateTime.now()));

            // when
            TableGroupResponse response = tableGroupService.create(request);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getCreatedDate()).isNotNull(),
                    () -> assertThat(response.getOrderTables().get(0)).extracting("empty").isEqualTo(false),
                    () -> assertThat(response.getOrderTables().get(0)).extracting("tableGroupId").isEqualTo(1L)
            );
        }

        private TableGroupCreateRequest 테이블_그룹_생성_dto를_만든다(final Long id, final LocalDateTime createdDate,
                                                           final List<OrderTable> orderTables) {
            return new TableGroupCreateRequest(id, createdDate, orderTables);
        }
    }

    @Nested
    class ungroup은 {

        @Test
        void order가_COOKING_또는_MEAL_상태이면_예외를_반환한다() {
            // given
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTable1, orderTable2));

            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                    Arrays.asList(1L, 2L),
                    Arrays.asList(COOKING.name(), MEAL.name()))).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void table_group을_취소할_수_있다() {
            // given
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(1L);
            orderTable1.setTableGroupId(1L);
            orderTable1.setEmpty(true);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            orderTable2.setTableGroupId(1L);
            orderTable2.setEmpty(true);
            when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTable1, orderTable2));

            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

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
