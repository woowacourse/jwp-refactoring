package kitchenpos.application;

import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;
import static kitchenpos.exception.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.tablegroup.CreateTableGroupCommand;
import kitchenpos.application.dto.tablegroup.CreateTableGroupResponse;
import kitchenpos.application.dto.tablegroup.UngroupTableGroupCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    @Test
    void 주문_테이블들이_없으면_예외가_발생한다() {
        // given
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블들이_하나면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(savedOrderTable.id()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, false);
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(orderTable1.id(), orderTable2.id()));

        // when & then
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_NOT_FOUND);
    }

    @Nested
    class 주문_테이블들이_있는경우 {

        @Test
        void 주문_테이블이_비어있지_않으면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = new OrderTable(0, false);
            OrderTable orderTable2 = new OrderTable(0, false);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
            CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(
                    savedOrderTable1.id(), savedOrderTable2.id()
            ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체를_지정한다() {
            // given
            OrderTable orderTable1 = new OrderTable(0, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            OrderTable orderTable2 = new OrderTable(0, true);
            OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
            CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(
                    savedOrderTable1.id(), savedOrderTable2.id()
            ));

            // when
            CreateTableGroupResponse result = tableGroupService.create(command);

            // then
            assertAll(
                    () -> assertThat(result.id()).isPositive(),
                    () -> assertThat(result.orderTableResponses()).hasSize(2)
            );
        }

        @Nested
        class 단체가_지정되어_있는경우 {

            @Test
            void 주문_테이블에_이미_지정된_단체가_있으면_예외가_발생한다() {
                // given
                TableGroup 지정된_그룹 = 빈_테이블들을_그룹으로_지정한다();
                OrderTable orderTable1 = new OrderTable(null, 지정된_그룹, 0, true);
                OrderTable orderTable2 = new OrderTable(0, true);
                OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
                OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
                CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(
                        savedOrderTable1.id(), savedOrderTable2.id()
                ));

                // when & then
                assertThatThrownBy(() -> tableGroupService.create(command))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 조리중이거나_식사중인_테이블의_그룹을_해제하면_예외가_발생한다() {
                // given
                OrderTable orderTable1 = new OrderTable(0, true);
                OrderTable orderTable2 = new OrderTable(0, true);
                TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
                TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

                Menu 맛있는_메뉴 = 맛있는_메뉴();
                OrderLineItem orderLineItem = new OrderLineItem(맛있는_메뉴, 1);
                Order order = new Order(null, orderTable1, OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(orderLineItem));
                orderRepository.save(order);

                UngroupTableGroupCommand command = new UngroupTableGroupCommand(savedTableGroup.id());

                // when
                BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                        tableGroupService.ungroup(command)
                ).exceptionType();

                // then
                assertThat(exceptionType).isEqualTo(CAN_NOT_UNGROUP_COOKING_OR_MEAL);
            }

            @Test
            void 그룹을_해제한다() {
                // given
                OrderTable orderTable1 = new OrderTable(0, true);
                OrderTable orderTable2 = new OrderTable(0, true);
                TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
                Long tableGroupId = tableGroupRepository.save(tableGroup).id();
                UngroupTableGroupCommand command = new UngroupTableGroupCommand(tableGroupId);

                // when
                tableGroupService.ungroup(command);
                Optional<TableGroup> result = tableGroupRepository.findById(tableGroupId);

                // then
                assertThat(result).isPresent();
                assertThat(result.get().orderTables()).isEmpty();
            }
        }
    }
}
