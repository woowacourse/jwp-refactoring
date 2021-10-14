package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableGroupService 단위 테스트")
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

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("TableGroup의 OrderTable 컬렉션 크기가 2 미만이면")
        @Nested
        class Context_order_table_size_smaller_than_two {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroup tableGroup = new TableGroup();
                tableGroup.setOrderTables(Collections.emptyList());

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("TableGroup에 속한 OrderTable은 최소 2개 이상이어야합니다.");
            }
        }

        @DisplayName("TableGroup의 OrderTable 컬렉션이 DB에 저장되어있지 않았다면")
        @Nested
        class Context_order_table_not_persisted {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroup tableGroup = new TableGroup();
                OrderTable orderTable = new OrderTable();
                OrderTable orderTable2 = new OrderTable();;
                tableGroup.setOrderTables(Arrays.asList(orderTable, orderTable2));
                orderTable.setId(1L);
                orderTable2.setId(2L);
                given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.emptyList());

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("요청한 OrderTable이 저장되어있지 않습니다.");

                verify(orderTableDao, times(1)).findAllByIdIn(any());
            }
        }

        @DisplayName("TableGroup에 속한 OrderTable 중 일부가 비어있지 않다면")
        @Nested
        class Context_order_table_not_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroup tableGroup = new TableGroup();
                OrderTable orderTable = new OrderTable();
                OrderTable orderTable2 = new OrderTable();;
                List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
                tableGroup.setOrderTables(orderTables);
                orderTable.setId(1L);
                orderTable.setEmpty(false);
                orderTable2.setId(2L);
                orderTable.setEmpty(true);
                given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable 일부가 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");

                verify(orderTableDao, times(1)).findAllByIdIn(any());
            }
        }

        @DisplayName("TableGroup에 속한 OrderTable 중 일부가 이미 특정 TableGroup에 속해있다면")
        @Nested
        class Context_order_table_already_in_table_group {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroup tableGroup = new TableGroup();
                OrderTable orderTable = new OrderTable();
                OrderTable orderTable2 = new OrderTable();;
                List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
                tableGroup.setOrderTables(orderTables);
                orderTable.setId(1L);
                orderTable.setEmpty(true);
                orderTable2.setId(2L);
                orderTable.setEmpty(true);
                orderTable2.setTableGroupId(31L);
                given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable 일부가 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");

                verify(orderTableDao, times(1)).findAllByIdIn(any());
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("TableGroup을 저장하고 반환한다.")
            @Test
            void it_saves_and_returns_table_group() {
                // given
                TableGroup tableGroup = new TableGroup();
                OrderTable orderTable = new OrderTable();
                OrderTable orderTable2 = new OrderTable();;
                List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
                tableGroup.setId(1L);
                tableGroup.setOrderTables(orderTables);
                orderTable.setId(1L);
                orderTable.setEmpty(true);
                orderTable2.setId(2L);
                orderTable2.setEmpty(true);
                given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
                given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

                // when
                TableGroup response = tableGroupService.create(tableGroup);

                // then
                assertThat(response).usingRecursiveComparison()
                    .isEqualTo(tableGroup);

                verify(orderTableDao, times(1)).findAllByIdIn(any());
                verify(tableGroupDao, times(1)).save(tableGroup);
                verify(orderTableDao, times(2)).save(any());
            }
        }
    }

    @DisplayName("ungroup 메서드는")
    @Nested
    class Describe_ungroup {

        @DisplayName("TableGroup에 속한 OrderTable에 속한 Order들이, 조리 중이거나 식사 중이라면")
        @Nested
        class Context_order_cooking_or_meal {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                OrderTable orderTable2 = new OrderTable();
                List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
                given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
                given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .willReturn(true);

                // when, then
                assertThatCode(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("TableGroup에 속한 Order 중 일부가 조리중이거나 식사 중입니다.");

                verify(orderTableDao, times(1)).findAllByTableGroupId(1L);
                verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("group을 정상 해지한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                OrderTable orderTable2 = new OrderTable();
                List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
                given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
                given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .willReturn(false);

                // when
                tableGroupService.ungroup(1L);

                verify(orderTableDao, times(1)).findAllByTableGroupId(1L);
                verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
                verify(orderTableDao, times(2)).save(any(OrderTable.class));
            }
        }
    }
}
