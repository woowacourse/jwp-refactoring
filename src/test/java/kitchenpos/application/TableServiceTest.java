package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fakedao.OrderFakeDao;
import kitchenpos.fakedao.OrderTableFakeDao;
import kitchenpos.fakedao.TableGroupFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TableServiceTest {

    private TableGroupDao tableGroupDao = new TableGroupFakeDao();
    private OrderDao orderDao = new OrderFakeDao();
    private OrderTableDao orderTableDao = new OrderTableFakeDao();

    private TableService tableService = new TableService(orderDao, orderTableDao);

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable(null, 3, false);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("모든 주문 테이블을 조회한다.")
    @Test
    void list() {
        // given
        orderTableDao.save(new OrderTable(null, 2, false));
        orderTableDao.save(new OrderTable(null, 2, false));

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 테이블을 비울 때")
    @Nested
    class ChangeEmpty {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            OrderTable updateOrderTable = new OrderTable(null, 2, true);

            // when
            OrderTable actual = tableService.changeEmpty(orderTable.getId(), updateOrderTable);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("주문 테이블을 찾지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // then
            assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTable(null, 2, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 그룹이 지정되어 있으면 예외를 발생시킨다.")
        @Test
        void existTableGroup_exception() {
            // given
            TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), new ArrayList<>()));
            OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 2, false));

            // when
            assertThatThrownBy(() -> tableService.changeEmpty(
                    orderTable.getId(), new OrderTable(null, 1, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 주문 상태가 조리중이거나 조리가 됐다면 예외를 발생시킨다.")
        @Test
        void existsOrderStatusIsCookingOrMeal_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>()));

            // when
            assertThatThrownBy(() -> tableService.changeEmpty(
                    orderTable.getId(), new OrderTable(null, 3, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블의 손님수를 수정할 때")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            OrderTable updateOrderTable = new OrderTable(null, 3, false);

            // when
            OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(3);
        }

        @DisplayName("손님의 수가 0보다 작으면 예외를 발생시킨다.")
        @Test
        void numberOfGuestsLessThanZero_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));
            OrderTable updateOrderTable = new OrderTable(null, -1, false);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블을 조회하지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // given
            OrderTable updateOrderTable = new OrderTable(null, -1, false);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, updateOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 empty 상태면 예외를 발생시킨다.")
        @Test
        void emptyIsTrue_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, true));
            OrderTable updateOrderTable = new OrderTable(null, 3, false);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
