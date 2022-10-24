package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    TableService tableService;

    @Autowired
    OrderService orderService;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderLineItemDao orderLineItemDao;

    @Autowired
    OrderDao orderDao;

    @DisplayName("테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable 테이블_1 = Fixtures.테이블_1();

        OrderTable saved = tableService.create(테이블_1);

        assertThat(tableService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("테이블 고객 수는 0 이상이어야 한다.")
    @Test
    void create_invalidCustomerCount() {
        assertThatThrownBy(() -> new OrderTable(1L, null, -1, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 고객 수는 0 이상이어야 한다.");
    }

    @DisplayName("테이블의 빈 상태 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable 테이블_1 = tableService.create(Fixtures.테이블_1());

        테이블_1.updateEmpty(false);
        tableService.changeEmpty(테이블_1.getId(), 테이블_1);

        assertThat(orderTableDao.findById(테이블_1.getId()).orElseThrow().isEmpty())
                .isFalse();
    }

    @DisplayName("테이블은 단체지정이 없어야 한다.")
    @Test
    void changeEmpty_noTableGroup() {
        tableGroupDao.save(Fixtures.테이블그룹(List.of(Fixtures.빈테이블_1(), Fixtures.빈테이블_2())));
        OrderTable 단체지정_테이블_1 = orderTableDao.save(new OrderTable(1L, 1L, 10, false));

        assertThatThrownBy(() -> tableService.changeEmpty(단체지정_테이블_1.getId(), 단체지정_테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 단체지정이 없어야 한다.");
    }

    @DisplayName("테이블의 주문이 있다면 COMPLETION 상태여야 한다.")
    @Test
    void changeEmpty_noOrderComplete() {
        OrderTable 테이블_1 = orderTableDao.save(Fixtures.테이블_1());
        Order order = orderService.create(Fixtures.주문_테이블1_후라이드());

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_1.getId(), 테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문이 있다면 COMPLETION 상태여야 한다.");
    }

    @DisplayName("특정 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable 테이블_1 = tableService.create(Fixtures.테이블_1());

        테이블_1.updateNumberOfGuests(100);
        tableService.changeNumberOfGuests(테이블_1.getId(), 테이블_1);

        assertThat(orderTableDao.findById(테이블_1.getId()).orElseThrow().getNumberOfGuests())
                .isEqualTo(100);
    }

    @DisplayName("테이블은 차있어야 한다.")
    @Test
    void changeNumberOfGuests_noFillTable() {
        OrderTable 빈테이블_1 = tableService.create(Fixtures.빈테이블_1());

        빈테이블_1.updateNumberOfGuests(100);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈테이블_1.getId(), 빈테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블은 차있어야 한다.");
    }

    @DisplayName("테이블 고객 수는 0 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_zeroCustomer() {
        OrderTable 빈테이블_1 = tableService.create(Fixtures.빈테이블_1());

        빈테이블_1.updateNumberOfGuests(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈테이블_1.getId(), 빈테이블_1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 고객 수는 0 이상이어야 한다.");
    }
}
