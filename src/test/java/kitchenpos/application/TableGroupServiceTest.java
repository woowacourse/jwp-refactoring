package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @Nested
    class CreateTest {
        @Test
        @DisplayName("orderTableId가 비어있으면 예외가 발생한다.")
        void orderTableIdsIsEmpty() {
            // given
            final TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        @DisplayName("하나의 그룹으로 묶을 테이블의 수가 2보다 작으면 예외가 발생한다.")
        void orderTableNum(int value) {
            // given
            final List<OrderTableIdDto> dtos = new ArrayList<>();
            for (int i = 0; i <= value; i++) {
                dtos.add(new OrderTableIdDto(1L));
            }
            final TableGroupRequest request = new TableGroupRequest(dtos);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청한 테이블이 데이터베이스에 모두 존재하지 않으면 예와가 발생한다.")
        void notMatchOrderTable() {
            // given
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableIdDto(1L), new OrderTableIdDto(2L), new OrderTableIdDto(3L))
            );
            final List<OrderTable> orderTables = List.of(mock(OrderTable.class), mock(OrderTable.class));
            given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청한 테이블이 이미 다른 그룹에 속해있다면 예외가 발생한다.")
        void alreadyHasGroup() {
            // given
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableIdDto(1L), new OrderTableIdDto(2L), new OrderTableIdDto(3L))
            );
            given(orderTableDao.findAllByIdIn(any())).willReturn(List.of(new OrderTable(null, 1L, 3, false)));

            // when. then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청한 테이블이 이미 차있다면 예외가 발생한다.")
        void tableIsNotEmpty() {
            // given
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableIdDto(1L), new OrderTableIdDto(2L), new OrderTableIdDto(3L))
            );
            given(orderTableDao.findAllByIdIn(any())).willReturn(List.of(new OrderTable(null, null, 3, false)));

            // when. then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블을 그룹을 생성한다.")
        void create() {
            // given
            final TableGroupRequest request = new TableGroupRequest(
                    List.of(new OrderTableIdDto(1L), new OrderTableIdDto(2L), new OrderTableIdDto(3L))
            );
            final List<OrderTable> orderTables = List.of(
                    new OrderTable(1L, null, 0, true),
                    new OrderTable(2L, null, 0, true),
                    new OrderTable(3L, null, 0, true)
            );
            final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
            given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
            given(tableGroupDao.save(any())).willReturn(tableGroup);

            // when
            final TableGroup result = tableGroupService.create(request);

            // then
            verify(orderTableDao, times(3)).save(any());
            assertAll(
                    () -> assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(result.getId()),
                    () -> assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(result.getId()),
                    () -> assertThat(orderTables.get(2).getTableGroupId()).isEqualTo(result.getId()),
                    () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                    () -> assertThat(orderTables.get(1).isEmpty()).isFalse(),
                    () -> assertThat(orderTables.get(2).isEmpty()).isFalse()
            );
        }
    }

    @Nested
    class UnGroupTest {
        @Test
        @DisplayName("그룹을 해제하려고 하는 테이블이 COOKING 혹은 MEAL 상태이면 예외가 밣생한다.")
        void existsByOrderTableIdInAndOrderStatusIn() {
            // given
            final List<OrderTable> orderTables = List.of(
                    new OrderTable(1L, 1L, 3, false),
                    new OrderTable(2L, 1L, 4, false)
            );
            given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블들의 그룹을 해제한다.")
        void ungroup() {
            // given
            final List<OrderTable> orderTables = List.of(
                    new OrderTable(1L, 1L, 3, true),
                    new OrderTable(2L, 1L, 4, true)
            );
            given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

            // when
            tableGroupService.ungroup(1L);

            // then
            verify(orderTableDao, times(2)).save(any());
        }
    }
}
