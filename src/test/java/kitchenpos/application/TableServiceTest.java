package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null, null, 0, false);
        OrderTable orderTable2 = new OrderTable(null, null, 0, false);

        tableService.create(orderTable1);
        tableService.create(orderTable2);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void 주문_테이블을_빈_상태로_변경한다(boolean status) {
        // given
        OrderTable savedOrderTable = tableService.create(new OrderTable(null, null, 0, false));

        OrderTable changedOrderTable = new OrderTable(null, null, 0, status);

        // when
        OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), changedOrderTable);

        // then
        assertThat(actual.isEmpty()).isEqualTo(status);
    }

    @Test
    void 존재하지_않는_주문_테이블의_상태를_변경할_수_없다() {
        // given
        long invalidOrderTable = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTable, new OrderTable(null, null,
                0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_테이블_그룹에_속한_주문_테이블의_상태를_변경할_수_없다() {
        // given
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), null);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable = new OrderTable(null, savedTableGroup.getId(), 0, false);
        OrderTable invalidOrderTable = orderTableDao.save(orderTable);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTable.getId(),
                new OrderTable(null, null, 0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 테이블이_주문목록에_존재하고_상태가_조리중이거나_식사중이면_테이블의_상태를_변경할_수_없다(String orderStatus) {
        //given
        OrderTable invalidOrderTable = getOrderedOrderTable(orderStatus);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTable.getId(), new OrderTable(null, null,
                0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_주문목록에_존재하고_이미_계산이_완료되었으면_테이블의_상태를_변경할_수_있다() {
        //given
        OrderTable invalidOrderTable = getOrderedOrderTable(OrderStatus.COMPLETION.name());

        // when & then
        assertThatCode(() -> tableService.changeEmpty(invalidOrderTable.getId(), new OrderTable(null, null,
                0, false)))
                .doesNotThrowAnyException();
    }

    @Test
    void 주문_테이블의_방문_손님의_수를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        int numberOfGuests = 100;

        // when
        OrderTable actual = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                new OrderTable(null, null, numberOfGuests, false));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    void 주문_테이블의_방문_손님의_수를_0_미만으로_변경할_수_없다(int numberOfGuests) {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(),
                new OrderTable(null, null, numberOfGuests, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지않는_주문_테이블의_방문_손님의_수를_변경할_수_없다() {
        // given
        long invalidOrderTableId = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, new OrderTable(null, null,
                0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태의_주문_테이블은_방문_손님의_수를_변경할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(),
                new OrderTable(null, null, 100, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable getOrderedOrderTable(String orderStatus) {
        OrderTable invalidOrderTable = orderTableDao.save(new OrderTable(null, null, 0, false));

        Order order = new Order();
        order.setOrderTableId(invalidOrderTable.getId());
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);
        return invalidOrderTable;
    }
}
