package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.CreateTableGroupCommand;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
    }

    @Test
    void 주문_테이블들이_null이면_예외가_발생한다() {
        // given
        CreateTableGroupCommand command = new CreateTableGroupCommand(null);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

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
        OrderTable orderTable = new OrderTable(null, null, 0, false);
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
        assertThatThrownBy(() -> tableGroupService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
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
            TableGroup result = tableGroupService.create(command);

            // then
            assertAll(
                    () -> assertThat(result.id()).isPositive(),
                    () -> assertThat(result.orderTables()).hasSize(2)
            );
        }

        @Nested
        class 단체가_지정되어_있는경우 {

            @Test
            void 주문_테이블에_이미_지정된_단체가_있으면_예외가_발생한다() {
                // given
                TableGroup 지정된_그룹 = 빈_테이블들을_그룹으로_지정한다();
                OrderTable orderTable1 = new OrderTable(지정된_그룹, 0, true);
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
                OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
                OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

                주문(savedOrderTable1, OrderStatus.COOKING, 맛있는_메뉴());
                CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(
                        savedOrderTable1.id(), savedOrderTable2.id()
                ));
                TableGroup savedTableGroup = tableGroupService.create(command);

                // when & then
                assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.id()))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
