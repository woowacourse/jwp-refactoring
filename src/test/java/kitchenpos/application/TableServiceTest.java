package kitchenpos.application;

import static kitchenpos.domain.fixture.OrderFixture.완료된_주문;
import static kitchenpos.domain.fixture.OrderFixture.요리중인_주문;
import static kitchenpos.domain.fixture.OrderTableFixture.새로운_테이블;
import static kitchenpos.domain.fixture.TableGroupFixture.새로운_테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeTableGroupDao;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.fixture.OrderTableFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Table 서비스 테스트")
class TableServiceTest {

    private TableService tableService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    private TableGroup 저장된_테이블_그룹;

    @BeforeEach
    void setUp() {
        final TableGroupDao tableGroupDao = new FakeTableGroupDao();
        orderDao = new FakeOrderDao();
        orderTableDao = new FakeOrderTableDao();
        tableService = new TableService(orderDao, orderTableDao);

        저장된_테이블_그룹 = tableGroupDao.save(새로운_테이블_그룹());
    }

    @DisplayName("테이블을 등록한다")
    @Test
    void create() {
        final OrderTableRequest request = new OrderTableRequest(1, false);

        final OrderTableResponse response = tableService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("테이블의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfOrderTable = 5;
        for (int i = 0; i < numberOfOrderTable; i++) {
            orderTableDao.save(OrderTableFixture.새로운_테이블());
        }

        final List<OrderTableResponse> responses = tableService.list();

        assertThat(responses).hasSize(numberOfOrderTable);
    }

    @DisplayName("테이블을 비운다")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = OrderTableFixture.새로운_테이블();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Orders order = 완료된_주문(savedOrderTable.getId());
        orderDao.save(order);

        final OrderTableRequest newOrderTable = new OrderTableRequest(savedOrderTable.getNumberOfGuests(), true);
        final OrderTableResponse changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 비울 때 주문 테이블이 존재해야 한다")
    @Test
    void changeEmptyOrderTableIsNotExist() {
        final long notSavedOrderTableId = 0L;

        assertThatThrownBy(() -> tableService.changeEmpty(notSavedOrderTableId, new OrderTableRequest(0, true)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 테이블 입니다.");
    }

    @DisplayName("테이블을 비울 때 저장되어 있는 테이블 그룹의 아이디가 null 이어야 한다")
    @Test
    void changeEmptyTableGroupIdIsNull() {
        final OrderTable orderTable = 새로운_테이블(저장된_테이블_그룹.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(0, true)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블 그룹이 존재합니다.");
    }

    @DisplayName("테이블을 비울 때 테이블의 주문 상태가 요리중이거나 식사중일 경우 테이블을 비울 수 없다")
    @Test
    void changeEmptyOrderStatusIsCompletion() {
        final OrderTable orderTable = OrderTableFixture.새로운_테이블();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Orders order = 요리중인_주문(savedOrderTable.getId());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest(0, true)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블의 주문이 완료되지 않았습니다.");
    }

    @DisplayName("테이블의 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = OrderTableFixture.비어있지_않는_테이블();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        int changedNumberOfGuests = 1;
        final OrderTableRequest newOrderTable = new OrderTableRequest(changedNumberOfGuests, true);
        final OrderTableResponse response = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

        assertThat(response.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
    }

    @DisplayName("테이블 손님의 수 변경 시 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsOrderTableIsNotExist() {
        long notSavedOrderTable = 0L;

        final OrderTableRequest newOrderTable = new OrderTableRequest(1, true);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notSavedOrderTable, newOrderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 테이블 입니다.");
    }
}
