package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeTableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.fixture.OrderFixture;
import kitchenpos.domain.fixture.OrderTableFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Table 서비스 테스트")
class TableServiceTest {

    private TableService tableService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    private TableGroup savedTableGroup;

    @BeforeEach
    void setUp() {
        final TableGroupDao tableGroupDao = new FakeTableGroupDao();
        orderDao = new FakeOrderDao();
        orderTableDao = new FakeOrderTableDao();

        tableService = new TableService(orderDao, orderTableDao);

        // table group
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup = tableGroupDao.save(tableGroup);
    }

    @DisplayName("테이블을 등록한다")
    @Test
    void create() {
        final OrderTable orderTable = OrderTableFixture.주문_테이블().build();

        final OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("테이블의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfOrderTable = 5;
        for (int i = 0; i < numberOfOrderTable; i++) {
            orderTableDao.save(OrderTableFixture.주문_테이블().build());
        }

        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(numberOfOrderTable);
    }

    @DisplayName("테이블을 비운다")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = OrderTableFixture.주문_테이블().build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문_상태(OrderStatus.COMPLETION.name())
            .주문한_시간(LocalDateTime.now())
            .build();
        orderDao.save(order);

        final OrderTable newOrderTable = OrderTableFixture.주문_테이블()
            .빈_테이블(true)
            .build();
        final OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 비울 때 주문 테이블이 존재해야 한다")
    @Test
    void changeEmptyOrderTableIsNotExist() {
        final long notSavedOrderTableId = 0L;

        assertThatThrownBy(() -> tableService.changeEmpty(notSavedOrderTableId, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비울 때 저장되어 있는 주문 테이블의 아이디가 null 이어야 한다")
    @Test
    void changeEmptyTableGroupIdIsNull() {
        final OrderTable orderTable = OrderTableFixture.주문_테이블()
            .테이블_그룹_아이디(savedTableGroup.getId())
            .build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비울 때 테이블의 주문 상태가 요리중이거나 식사중일 경우 테이블을 비울 수 없다")
    @Test
    void changeEmptyOrderStatusIsCompletion() {
        final OrderTable orderTable = OrderTableFixture.주문_테이블().build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = OrderFixture.주문()
            .주문_테이블_아이디(savedOrderTable.getId())
            .주문_상태(OrderStatus.COOKING.name())
            .주문한_시간(LocalDateTime.now())
            .build();
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = OrderTableFixture.주문_테이블().build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable newOrderTable = OrderTableFixture.주문_테이블()
            .손님의_수(1)
            .build();

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("테이블 손님의 수 변경 시 변경하려는 손님의 수는 0보다 커야한다")
    @Test
    void changeNumberOfGuestsNumberIsLowerZero() {
        final OrderTable newOrderTable = OrderTableFixture.주문_테이블()
            .손님의_수(0)
            .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님의 수 변경 시 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsOrderTableIsNotExist() {
        final OrderTable newOrderTable = OrderTableFixture.주문_테이블()
            .손님의_수(1)
            .build();

        long notSavedOrderTable = 0L;
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notSavedOrderTable, newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님의 수 변경 시 테이블이 비어있으면 안된다")
    @Test
    void changeNumberOfGuestsOrderTableIsEmpty() {
        final OrderTable orderTable = OrderTableFixture.주문_테이블()
            .빈_테이블(true)
            .build();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable newOrderTable = OrderTableFixture.주문_테이블()
            .손님의_수(1)
            .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
