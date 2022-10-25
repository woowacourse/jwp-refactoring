package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.SpringBootNestedTest;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("테이블 정보를 생성하면 ID가 할당된 OrderTable객체가 반환된다")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();

        OrderTable actual = tableService.create(orderTable);
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하는 모든 테이블 목록을 조회한다")
    @Test
    void list() {
        OrderTable orderTable = new OrderTable();
        orderTableDao.save(orderTable);
        orderTableDao.save(orderTable);
        orderTableDao.save(orderTable);

        List<OrderTable> actual = tableService.list();
        assertThat(actual).hasSize(3);
    }

    @DisplayName("테이블을 비어있는 테이블로 설정한다")
    @SpringBootNestedTest
    class ChangeEmptyTest {

        @DisplayName("테이블을 비어있는 테이블로 설정한다")
        @Test
        void changeEmpty() {
            OrderTable newOrderTable = new OrderTable();
            OrderTable orderTable = orderTableDao.save(newOrderTable);

            Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            orderDao.save(order);

            OrderTable emptyOrderTable = new OrderTable();
            emptyOrderTable.setEmpty(true);

            OrderTable actual = tableService.changeEmpty(orderTable.getId(), emptyOrderTable);
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("존재하지 않는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistOrderTable() {
            Long notExistId = 0L;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistId, new OrderTable()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 다른 테이블 그룹에 묶여있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseTableBelongsToTableGroup() {
            TableGroup newTableGroup = new TableGroup();
            newTableGroup.setCreatedDate(LocalDateTime.now());
            TableGroup tableGroup = tableGroupDao.save(newTableGroup);

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setTableGroupId(tableGroup.getId());
            newOrderTable.setEmpty(true);
            OrderTable orderTable = orderTableDao.save(newOrderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블 중 주문 상태가 Cooking, Meal인 주문이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOrderStatusIsCookingOrMeal() {
            OrderTable newOrderTable = new OrderTable();
            OrderTable orderTable = tableService.create(newOrderTable);

            Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(1L, 3)));
            order.setOrderStatus(OrderStatus.COOKING.name());
            orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("손님 수를 변경한다")
    @SpringBootNestedTest
    class ChangeNumberOfGuestsTest {

        OrderTable orderTable;

        @BeforeEach
        void setUp() {
            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(false);
            orderTable = tableService.create(newOrderTable);
        }

        @DisplayName("손님수를 올바르게 변경한다")
        @Test
        void changeNumberOfGuests() {
            int numberOfGuests = 10;
            OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(numberOfGuests);

            OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTableToUpdate);
            assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
        }

        @DisplayName("변경하려는 손님 수가 음수일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNegativeNumberOfGuests() {
            int numberOfGuests = -1;

            OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(false);
            OrderTable orderTable = tableService.create(newOrderTable);

            OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(numberOfGuests);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블 Id일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistGroupTable() {
            Long notExistId = 0L;

            OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistId, orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 비어있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable();
            newEmptyTable.setEmpty(true);
            OrderTable emptyTable = tableService.create(newEmptyTable);

            OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
