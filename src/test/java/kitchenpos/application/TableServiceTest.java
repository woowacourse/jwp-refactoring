package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.orderTable.OrderTableChangeEmptyRequest;
import kitchenpos.dto.orderTable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.orderTable.OrderTableCreateRequest;
import kitchenpos.dto.orderTable.OrderTableResponse;
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
        final OrderTableCreateRequest request = new OrderTableCreateRequest(0, false);

        // when
        final OrderTableResponse actual = tableService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroupId()).isNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isEqualTo(request.isEmpty())
        );
    }

    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        final OrderTableCreateRequest request1 = new OrderTableCreateRequest(0, false);
        final OrderTableCreateRequest request2 = new OrderTableCreateRequest(0, false);

        tableService.create(request1);
        tableService.create(request2);

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void 주문_테이블을_빈_상태로_변경한다(boolean status) {
        // given
        final OrderTableResponse savedOrderTable = tableService.create(new OrderTableCreateRequest(0, false));

        final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(status);

        // when
        OrderTableResponse actual = tableService.changeEmpty(savedOrderTable.getId(), request);

        // then
        assertThat(actual.isEmpty()).isEqualTo(status);
    }

    @Test
    void 존재하지_않는_주문_테이블의_상태를_변경할_수_없다() {
        // given
        long invalidOrderTable = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTable, new OrderTableChangeEmptyRequest(false)))
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
        assertThatThrownBy(
                () -> tableService.changeEmpty(invalidOrderTable.getId(), new OrderTableChangeEmptyRequest(false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 테이블이_주문목록에_존재하고_상태가_조리중이거나_식사중이면_테이블의_상태를_변경할_수_없다(String orderStatus) {
        //given
        OrderTable invalidOrderTable = getOrderedOrderTable(orderStatus);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(invalidOrderTable.getId(), new OrderTableChangeEmptyRequest(false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_주문목록에_존재하고_이미_계산이_완료되었으면_테이블의_상태를_변경할_수_있다() {
        //given
        OrderTable invalidOrderTable = getOrderedOrderTable(OrderStatus.COMPLETION.name());

        // when & then
        assertThatCode(
                () -> tableService.changeEmpty(invalidOrderTable.getId(), new OrderTableChangeEmptyRequest(false))
        ).doesNotThrowAnyException();
    }

    @Test
    void 주문_테이블의_방문_손님의_수를_변경한다() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, false);
        OrderTableResponse response = tableService.create(request);

        int numberOfGuests = 100;

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(response.getId(),
                new OrderTableChangeNumberOfGuestsRequest(numberOfGuests));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    void 주문_테이블의_방문_손님의_수를_0_미만으로_변경할_수_없다(int numberOfGuests) {
        // given
        final OrderTableCreateRequest request = new OrderTableCreateRequest(0, false);
        OrderTableResponse response = tableService.create(request);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(response.getId(),
                new OrderTableChangeNumberOfGuestsRequest(numberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지않는_주문_테이블의_방문_손님의_수를_변경할_수_없다() {
        // given
        long invalidOrderTableId = -1L;

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId,
                new OrderTableChangeNumberOfGuestsRequest(0)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태의_주문_테이블은_방문_손님의_수를_변경할_수_없다() {
        // given
        final OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);
        OrderTableResponse response = tableService.create(request);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(response.getId(),
                new OrderTableChangeNumberOfGuestsRequest(100)))
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
