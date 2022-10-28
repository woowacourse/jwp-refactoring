package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import kitchenpos.application.dto.request.ChangeNumOfTableGuestsRequest;
import kitchenpos.application.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
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
        OrderTableRequest request = new OrderTableRequest(3, false);

        OrderTableResponse actual = tableService.create(request);
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하는 모든 테이블 목록을 조회한다")
    @Test
    void list() {
        OrderTable orderTable = new OrderTable(3, false);
        orderTableDao.save(orderTable);
        orderTableDao.save(orderTable);
        orderTableDao.save(orderTable);

        List<OrderTableResponse> actual = tableService.list();
        assertThat(actual).hasSize(3);
    }

    @DisplayName("테이블을 비어있는 테이블로 설정한다")
    @SpringBootNestedTest
    class ChangeEmptyTest {

        ChangeOrderTableEmptyRequest changeOrderTableEmptyRequest = new ChangeOrderTableEmptyRequest(true);

        @DisplayName("테이블을 비어있는 테이블로 설정한다")
        @Test
        void changeEmpty() {
            OrderTable newOrderTable = new OrderTable(3, false);
            OrderTable orderTable = orderTableDao.save(newOrderTable);

            Order order = Order.create(orderTable.getId(), List.of());
            order.changeStatus(OrderStatus.COMPLETION);
            orderDao.save(order);

            OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), changeOrderTableEmptyRequest);
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("존재하지 않는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistOrderTable() {
            Long notExistId = 0L;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistId, changeOrderTableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 다른 테이블 그룹에 묶여있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseTableBelongsToTableGroup() {
            TableGroup newTableGroup = new TableGroup();
            TableGroup tableGroup = tableGroupDao.save(newTableGroup);

            OrderTable newOrderTable = new OrderTable(3, true);
            newOrderTable.joinTableGroup(tableGroup.getId());
            OrderTable orderTable = orderTableDao.save(newOrderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블 중 주문 상태가 Cooking, Meal인 주문이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOrderStatusIsCookingOrMeal() {
            OrderTable newOrderTable = new OrderTable(3, false);
            OrderTable orderTable = orderTableDao.save(newOrderTable);

            Order order = Order.create(orderTable.getId(), List.of(new OrderLineItem(1L, 3)));
            order.changeStatus(OrderStatus.COOKING);
            orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("손님 수를 변경한다")
    @SpringBootNestedTest
    class ChangeNumberOfGuestsTest {

        OrderTable orderTable;

        @BeforeEach
        void setUp() {
            OrderTable newOrderTable = new OrderTable(3, false);
            newOrderTable.changeEmptyStatus(false);
            orderTable = orderTableDao.save(newOrderTable);
        }

        @DisplayName("손님수를 올바르게 변경한다")
        @Test
        void changeNumberOfGuests() {
            int numberOfGuests = 10;
            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(numberOfGuests);

            OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), changeGuestsRequest);
            assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
        }

        @DisplayName("변경하려는 손님 수가 음수일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNegativeNumberOfGuests() {
            int numberOfGuests = -1;

            OrderTable newOrderTable = new OrderTable(3, false);
            OrderTable orderTable = orderTableDao.save(newOrderTable);

            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(numberOfGuests);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블 Id일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistGroupTable() {
            Long notExistId = 0L;

            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistId, changeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 비어있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable(10, true);
            OrderTable emptyTable = orderTableDao.save(newEmptyTable);

            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), changeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
