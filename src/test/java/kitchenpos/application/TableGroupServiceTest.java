package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("TableGroupService 테스트")
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
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("OrderTable 리스트가 비어있거나 사이즈가 2 미만이면")
        class Context_with_orderTables_empty_or_size_less_than_two {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                TableGroup tableGroup = new TableGroup();
                tableGroup.setOrderTables(Collections.emptyList());

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("TableGroup의 OrderTable 리스트와 실제 db에 저장된 OrderTable 리스트의 사이즈가 다르면")
        class Context_with_orderTables_size_different {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(2L);
                anotherOrderTable.setEmpty(true);

                TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                tableGroup.setOrderTables(Arrays.asList(orderTable, anotherOrderTable));

                given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId())))
                        .willReturn(Collections.emptyList());

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should()
                        .findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()));
                then(tableGroupDao)
                        .should(never())
                        .save(tableGroup);
                then(orderTableDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("조회한 OrderTable 리스트의 일부 OrderTable이 빈 테이블이 아니라면")
        class Context_with_some_orderTable_not_empty {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(2L);
                anotherOrderTable.setEmpty(false);

                TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                tableGroup.setOrderTables(Arrays.asList(orderTable, anotherOrderTable));

                given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId())))
                        .willReturn(Arrays.asList(orderTable, anotherOrderTable));

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should()
                        .findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()));
                then(tableGroupDao)
                        .should(never())
                        .save(tableGroup);
                then(orderTableDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("조회한 OrderTable 리스트의 일부 OrderTable의 tableGroupId가 null이 아니면")
        class Context_with_some_orderTable_tableGroupId_not_null {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(2L);
                anotherOrderTable.setTableGroupId(2L);
                anotherOrderTable.setEmpty(true);

                TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                tableGroup.setOrderTables(Arrays.asList(orderTable, anotherOrderTable));

                given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId())))
                        .willReturn(Arrays.asList(orderTable, anotherOrderTable));

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should()
                        .findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()));
                then(tableGroupDao)
                        .should(never())
                        .save(tableGroup);
                then(orderTableDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @Test
            @DisplayName("테이블 그룹을 반환한다")
            void it_return_table_group() {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(2L);
                anotherOrderTable.setEmpty(true);

                TableGroup tableGroup = new TableGroup();
                tableGroup.setId(1L);
                tableGroup.setOrderTables(Arrays.asList(orderTable, anotherOrderTable));

                given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId())))
                        .willReturn(Arrays.asList(orderTable, anotherOrderTable));
                given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

                TableGroup createdTableGroup = tableGroupService.create(tableGroup);

                assertThat(createdTableGroup).isEqualTo(tableGroup);
                then(orderTableDao)
                        .should()
                        .findAllByIdIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()));
                then(tableGroupDao)
                        .should()
                        .save(tableGroup);
                then(orderTableDao)
                        .should(times(2))
                        .save(any());
            }
        }

    }

    @Nested
    @DisplayName("ungroup 메소드는")
    class Describe_ungroup {

        @Nested
        @DisplayName("TableGroup id에 해당되는 일부 OrderTable이 아직 COOKING이나 MEAL 상태라면")
        class Context_with_orderTable_status_cooking_or_meal {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                Long tableGroupId = 1L;

                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setTableGroupId(tableGroupId);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(2L);
                anotherOrderTable.setTableGroupId(tableGroupId);
                anotherOrderTable.setEmpty(true);

                given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(Arrays.asList(orderTable, anotherOrderTable));
                given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()),
                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                        .isInstanceOf(IllegalArgumentException.class);

                then(orderTableDao)
                        .should()
                        .findAllByTableGroupId(tableGroupId);
                then(orderDao)
                        .should()
                        .existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()),
                                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
                then(orderTableDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_correct_case {

            @Test
            @DisplayName("OrderTable의 tableGroupId를 null로 수정한다")
            void it_tableGroupId_update_null() {
                Long tableGroupId = 1L;

                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setTableGroupId(tableGroupId);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(2L);
                anotherOrderTable.setTableGroupId(tableGroupId);
                anotherOrderTable.setEmpty(true);

                given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(Arrays.asList(orderTable, anotherOrderTable));
                given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()),
                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

                tableGroupService.ungroup(tableGroupId);

                then(orderTableDao)
                        .should()
                        .findAllByTableGroupId(tableGroupId);
                then(orderDao)
                        .should()
                        .existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId(), anotherOrderTable.getId()),
                                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
                then(orderTableDao)
                        .should(times(2))
                        .save(any());
            }
        }

    }

}