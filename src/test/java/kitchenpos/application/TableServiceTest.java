package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.DomainFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Sql(value = "/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("Table 생성")
    @Test
    void create() {
        OrderTable orderTable = DomainFactory.createOrderTable(null, 0, null, false);

        OrderTable actual = tableService.create(orderTable);

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Table 전체 조회")
    @Test
    void list() {
        orderTableDao.save(DomainFactory.createOrderTable(null, 1, null, false));

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(1);
    }

    @DisplayName("테이블이 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTable = DomainFactory.createOrderTable(null, 0, null, true);
        OrderTable saved = orderTableDao.save(orderTable);

        OrderTable expect = DomainFactory.createOrderTable(null, 1, null, false);
        OrderTable actual = tableService.changeEmpty(saved.getId(), expect);

        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("테이블이 비어있는지 여부 변경시 table group Id가 null이 아닐 때 예외 테스트")
    @Test
    void changeEmptyNonNullTableGroupId() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        OrderTable orderTable = DomainFactory.createOrderTable(null, 0, savedTableGroup.getId(), true);
        OrderTable saved = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(saved.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있는지 여부 변경시 주문 상태가 식사중이거나 조리중일 때 예외 테스트")
    @Test
    void changeEmptyWrongOrderStatus() {
        OrderTable orderTable = DomainFactory.createOrderTable(null, 0, null, false);
        OrderTable saved = orderTableDao.save(orderTable);
        Order order = DomainFactory.createOrder(null, OrderStatus.MEAL.name(), saved.getId(), LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(saved.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 바꾸기")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = DomainFactory.createOrderTable(null, 7, null, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable expect = DomainFactory.createOrderTable(null, 2, null, false);

        OrderTable changed = tableService.changeNumberOfGuests(savedOrderTable.getId(), expect);

        assertThat(changed.getNumberOfGuests()).isEqualTo(expect.getNumberOfGuests());
    }

    @DisplayName("테이블 바꿀 손님 수 0명 미만일 때 예외 발생")
    @Test
    void changeWrongNumberOfGuests() {
        OrderTable wrongNumberOfGuest = DomainFactory.createOrderTable(null, -1, null, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, wrongNumberOfGuest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있을 때 손님 수 변경 예외 발생")
    @Test
    void changeEmptyTableNumberOfGuests() {
        OrderTable emptyTable = DomainFactory.createOrderTable(null, 1, null, true);
        OrderTable savedOrderTable = orderTableDao.save(emptyTable);
        OrderTable orderTable = DomainFactory.createOrderTable(null, 3, null, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}