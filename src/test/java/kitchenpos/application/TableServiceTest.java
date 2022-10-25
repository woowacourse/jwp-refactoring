package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TableServiceTest {

    private TableService sut;
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        sut = new TableService(orderDao, orderTableDao);
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("새로운 주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when
        final OrderTable createdOrderTable = sut.create(orderTable);

        // then
        assertThat(createdOrderTable).isNotNull();
        assertThat(createdOrderTable.getId()).isNotNull();
        final OrderTable foundOrderTable = orderTableDao.findById(createdOrderTable.getId()).get();
        assertThat(foundOrderTable)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(createdOrderTable);
    }

    @DisplayName("전체 주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<OrderTable> orderTables = sut.list();

        // then
        assertThat(orderTables)
                .hasSize(8)
                .extracting("numberOfGuests", "empty")
                .containsExactlyInAnyOrder(
                        tuple(0, true),
                        tuple(0, true),
                        tuple(0, true),
                        tuple(0, true),
                        tuple(0, true),
                        tuple(0, true),
                        tuple(0, true),
                        tuple(0, true)
                );
    }

    @DisplayName("주문 테이블을 비어있는 상태로 변경할 수 있다.")
    @Test
    void changeTableEmpty() {
        // given
        final Long orderTableId = 1L;
        final OrderTable orderTable = new OrderTable(0, true);

        // when
        final OrderTable changedOrderTable = sut.changeEmpty(orderTableId, orderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 그룹(id)은 비어 있지 않으면 주문 테이블 상태를 비어있는 상태로 변경할 수 없다.")
    @Test
    void canNotChangeEmptyWhenGroupIdNotNull() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final OrderTable orderTable = new OrderTable(0, true);
        orderTable.setTableGroupId(savedTableGroup.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // when & then
        assertThatThrownBy(() -> sut.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있는 상태가 되려면 주문 테이블이 조리 중이거나 식사중인 상태이면 안된다.")
    @Test
    void canNotChangeTableWhenCookingOrMeal() {
        // given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
        final Long orderTableId = orderTable.getId();

        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        final Order order = new Order(orderTableId, "COOKING", LocalDateTime.now(), List.of(orderLineItem));
        orderService.create(order);

        // when & then
        assertThatThrownBy(() -> sut.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 조회결과가 없는 경우 테이블을 빈 상태로 변경할 수 없다.")
    @Test
    void changeEmptyWithEmptyOrderTable() {
        // given
        final long invalidOrderTableId = -1L;
        final OrderTable orderTable = new OrderTable(0, false);

        // when & then
        assertThatThrownBy(() -> sut.changeEmpty(invalidOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void canChangeGuestsCount() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = sut.create(orderTable);
        final Long orderTableId = createdOrderTable.getId();

        // when
        final OrderTable changedOrderTable = sut.changeNumberOfGuests(orderTableId, orderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("손님의 수는 0보다 작을 수 없다.")
    @Test
    void GuestsCountCanNotLessThenZero() {
        // given
        final OrderTable orderTable = new OrderTable(-1, false);
        final OrderTable createdOrderTable = sut.create(orderTable);
        final Long orderTableId = createdOrderTable.getId();

        // when & then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, orderTable));
    }

    @DisplayName("주문 테이블의 손님 수를 변경하려면 주문 테이블은 비어있으면 안된다.")
    @Test
    void tableEmptyWhenChangeGuestsCount() {
        // given
        final OrderTable orderTable = new OrderTable(1, true);
        final OrderTable createdOrderTable = sut.create(orderTable);
        final Long orderTableId = createdOrderTable.getId();

        // when & then
        assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTableId, orderTable));
    }
}
