package kitchenpos.tablegroup.application;

import kitchenpos.order.OrderStatus;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.ordertable.Empty;
import kitchenpos.ordertable.NumberOfGuests;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.application.OrderTableRepository;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.application.request.OrderTableIdDto;
import kitchenpos.tablegroup.application.request.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Nested
    class CreateTest {
        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        @DisplayName("하나의 그룹으로 묶을 테이블의 수가 2보다 작으면 예외가 발생한다.")
        void orderTableNum(int value) {
            // given
            final TableGroupRequest request = mock(TableGroupRequest.class);
            final List<OrderTableIdDto> dtos = new ArrayList<>();
            for (int i = 0; i <= value; i++) {
                dtos.add(mock(OrderTableIdDto.class));
            }
            given(request.getOrderTables()).willReturn(dtos);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Nested
        class SameRequestTest {
            private TableGroupRequest request;
            private List<OrderTableIdDto> dtos;

            @BeforeEach
            void setUp() {
                request = mock(TableGroupRequest.class);
                dtos = List.of(new OrderTableIdDto(1L), new OrderTableIdDto(2L));
            }

            @Test
            @DisplayName("요청한 테이블이 데이터베이스에 모두 존재하지 않으면 예외가 발생한다.")
            void notMatchOrderTable() {
                // given
                given(request.getOrderTables()).willReturn(dtos);

                final List<OrderTable> orderTables = List.of(mock(OrderTable.class));
                given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);

                // when, then
                assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("요청한 테이블이 이미 다른 테이블 그룹에 속해있다면 예외가 발생한다.")
            void alreadyHasGroup() {
                // given
                given(request.getOrderTables()).willReturn(dtos);

                final OrderTable orderTable = mock(OrderTable.class);
                final List<OrderTable> orderTables = List.of(orderTable, mock(OrderTable.class));
                given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
                given(orderTable.isEmpty()).willReturn(false);

                // when. then
                assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("요청한 테이블이 이미 다른 테이블 그룹 ID를 가지고 있다면 예외가 발생한다.")
            void alreadyHasAnotherTableGroupId() {
                // given
                given(request.getOrderTables()).willReturn(dtos);

                final OrderTable orderTable = mock(OrderTable.class);
                final List<OrderTable> orderTables = List.of(orderTable, mock(OrderTable.class));
                given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
                given(orderTable.isEmpty()).willReturn(true);
                given(orderTable.getTableGroupId()).willReturn(10L);

                // when. then
                assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("테이블을 그룹을 생성한다.")
            void create() {
                // given
                given(request.getOrderTables()).willReturn(dtos);

                final List<OrderTable> orderTables = List.of(
                        new OrderTable(new NumberOfGuests(2), Empty.EMPTY),
                        new OrderTable(new NumberOfGuests(3), Empty.EMPTY)
                );
                final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
                given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
                given(tableGroupRepository.save(any())).willReturn(tableGroup);

                // when
                final TableGroup result = tableGroupService.create(request);

                // then
                assertThat(result).usingRecursiveComparison().isEqualTo(tableGroup);
            }
        }
    }

    @Nested
    class UnGroupTest {
        @Test
        @DisplayName("그룹을 해제하려고 하는 테이블이 COOKING 혹은 MEAL 상태이면 예외가 발생한다.")
        void existsByOrderTableIdInAndOrderStatusIn() {
            // given
            final List<OrderTable> orderTables = List.of(
                    new OrderTable(1L, 1L, new NumberOfGuests(2), Empty.NOT_EMPTY),
                    new OrderTable(2L, 1L, new NumberOfGuests(3), Empty.NOT_EMPTY)
            );
            given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);
            given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                    .willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블들의 그룹을 해제한다.")
        void ungroup() {
            // given
            final List<OrderTable> orderTables = List.of(
                    new OrderTable(1L, 1L, new NumberOfGuests(2), Empty.EMPTY),
                    new OrderTable(2L, 1L, new NumberOfGuests(3), Empty.EMPTY)
            );
            given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);
            given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                    .willReturn(false);

            // when
            tableGroupService.ungroup(1L);

            // then
            assertSoftly(softly -> {
                verify(orderTableRepository, times(2)).save(any());
                assertThat(orderTables).extracting("tableGroupId")
                        .allSatisfy(orderTable -> assertThat(orderTable).isNull());
                assertThat(orderTables).extracting("empty")
                        .allSatisfy(empty -> assertThat(empty).isEqualTo(false));
            });
        }
    }
}
