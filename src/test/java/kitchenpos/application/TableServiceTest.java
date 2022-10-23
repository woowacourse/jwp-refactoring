package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 생성한_테이블을_반환한다() {
            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(1);
            newOrderTable.setEmpty(false);

            OrderTable actual = tableService.create(newOrderTable);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(1),
                    () -> assertThat(actual.getTableGroupId()).isNull(),
                    () -> assertThat(actual.isEmpty()).isFalse()
            );
        }

        @Test
        void 빈_테이블_여부가_누락된_경우_주문_테이블로_간주한다() {
            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(1);

            OrderTable actual = tableService.create(newOrderTable);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(1),
                    () -> assertThat(actual.isEmpty()).isFalse()
            );
        }

        @Test
        void 고객_인원_정보가_누락된_경우_0명으로_간주한다() {
            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(true);

            OrderTable actual = tableService.create(newOrderTable);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isZero(),
                    () -> assertThat(actual.isEmpty()).isTrue()
            );
        }
    }

    @Test
    void list_메서드는_모든_테이블_목록을_조회한다() {
        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSizeGreaterThan(1);
    }

    @DisplayName("changeEmpty 메서드는")
    @Nested
    class ChangeEmptyTest {

        @Test
        void 존재하지_않는_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);

            assertThatThrownBy(() -> tableService.changeEmpty(999999L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체로_지정된_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);

            long savedOrderTableId = 1L;
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setId(savedOrderTableId);
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(2L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            tableGroupService.create(tableGroup);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_들어간_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            OrderTable newOrderTable = new OrderTable();
            Long notEmptyOrderTableId = tableService.create(newOrderTable).getId();

            Order order = new Order();
            order.setOrderTableId(notEmptyOrderTableId);
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            orderLineItem.setQuantity(1);
            order.setOrderLineItems(List.of(orderLineItem));
            orderService.create(order);

            assertThatThrownBy(() -> tableService.changeEmpty(notEmptyOrderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("changeNumberOfGuests 메서드는")
    @Nested
    class ChangeNumberOfGuestsTest {

        @Test
        void 주문_테이블의_고객_수를_수정하고_수정된_테이블_정보를_반환한다() {
            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(1);
            newOrderTable.setEmpty(false);
            Long savedOrderTableId = tableService.create(newOrderTable).getId();

            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);

            OrderTable actual = tableService.changeNumberOfGuests(savedOrderTableId, orderTable);
            assertAll(
                    () -> assertThat(actual.getId()).isEqualTo(savedOrderTableId),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(5)
            );
        }

        @Test
        void 테이블의_고객_수를_음수로_수정하려는_경우_예외를_발생시킨다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(-1);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블의_고객_수를_수정하려는_경우_예외를_발생시킨다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(999999L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블의_고객_수를_수정하려는_경우_예외를_발생시킨다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(10);

            OrderTable savedOrderTable = orderTableDao.findById(5L).get();
            savedOrderTable.setEmpty(true);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
