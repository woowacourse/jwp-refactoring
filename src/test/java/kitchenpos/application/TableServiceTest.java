package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
        tableService.create(orderTable);
        tableService.create(orderTable);
        tableService.create(orderTable);

        List<OrderTable> actual = tableService.list();
        assertThat(actual).hasSize(3);
    }

    @DisplayName("테이블을 비어있는 테이블로 설정한다")
    @Nested
    class ChangeEmptyTest {

        @DisplayName("테이블을 비어있는 테이블로 설정한다")
        @Test
        void changeEmpty() {
            OrderTable orderTable = new OrderTable();
            OrderTable savedOrderTable = orderTableDao.save(orderTable);

            Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            orderDao.save(order);

            OrderTable emptyOrderTable = new OrderTable();
            emptyOrderTable.setEmpty(true);

            OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), emptyOrderTable);
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
            TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(LocalDateTime.now());
            TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

            OrderTable orderTable = new OrderTable();
            orderTable.setTableGroupId(savedTableGroup.getId());
            orderTable.setNumberOfGuests(5);
            orderTable.setEmpty(true);
            OrderTable savedOrderTable = orderTableDao.save(orderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블에서 주문 상태가 Cooking, Meal인 주문이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOrderStatusIsCookingOrMeal() {
            OrderTable orderTable = new OrderTable();
            OrderTable savedOrderTable = tableService.create(orderTable);

            Order order = new Order(savedOrderTable.getId(), List.of(new OrderLineItem(1L, 3)));
            order.setOrderStatus(OrderStatus.COOKING.name());
            orderDao.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
