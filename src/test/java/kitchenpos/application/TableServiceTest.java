package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyStatusUpdateRequest;
import kitchenpos.dto.request.TableNumberOfGuestsUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({
        TableService.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateTableGroupDao.class
})
class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("테이블을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        TableCreateRequest request = new TableCreateRequest(0, true);

        // when
        OrderTable actual = tableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTableDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            softly.assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
        });
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        TableCreateRequest request1 = new TableCreateRequest(0, true);
        TableCreateRequest request2 = new TableCreateRequest(0, true);

        OrderTable 주문테이블1 = tableService.create(request1);
        OrderTable 주문테이블2 = tableService.create(request2);

        // when
        List<OrderTable> list = tableService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(주문테이블1, 주문테이블2));
        });
    }

    @DisplayName("테이블의 empty 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable 주문테이블 = tableService.create(new TableCreateRequest(0, true));

        // when & then
        assertThat(주문테이블.isEmpty()).isEqualTo(true);

        TableEmptyStatusUpdateRequest request1 = new TableEmptyStatusUpdateRequest(false);
        OrderTable Empty_상태가_FALSE_변경된_주문테이블 = tableService.changeEmpty(주문테이블.getId(), request1);
        assertThat(Empty_상태가_FALSE_변경된_주문테이블.isEmpty()).isEqualTo(false);

        TableEmptyStatusUpdateRequest request2 = new TableEmptyStatusUpdateRequest(true);
        OrderTable Empty_상태가_TRUE_변경된_주문테이블 = tableService.changeEmpty(주문테이블.getId(), request2);
        assertThat(Empty_상태가_TRUE_변경된_주문테이블.isEmpty()).isEqualTo(true);
    }

    @DisplayName("테이블 empty 상태 변경 시, 주문 테이블을 찾을 수 없는 경우 예외가 발생한다.")
    @Test
    void changeEmpty_FailWithInvalidOrderTableId() {
        // given
        TableEmptyStatusUpdateRequest request = new TableEmptyStatusUpdateRequest(false);

        long invalidOrderTableId = 1000L;

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("테이블 empty 상태 변경 시, 테이블 그룹 ID가 null이 아닌 경우 예외가 발생한다.")
    @Test
    void changeEmpty_FailWithInvalidTableGroupId() {
        // given
        OrderTable orderTable1 = OrderTable.create(0, true);
        OrderTable orderTable2 = OrderTable.create(0, true);

        TableGroup tableGroup = TableGroup.groupOrderTables(List.of(orderTable1, orderTable2));
        TableGroup 테이블그룹 = tableGroupDao.save(tableGroup);

        orderTable1.setTableGroupId(테이블그룹.getId());
        OrderTable 주문테이블 = orderTableDao.save(orderTable1);

        TableEmptyStatusUpdateRequest request = new TableEmptyStatusUpdateRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 상태 변경 시, 주문의 상태가 COOKING 또는 MEAL인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_FailWithInvalidOrderStatus(String invalidOrderStatus) {
        // given
        OrderTable orderTable = orderTableDao.save(OrderTable.create(0, true));

        Order order = Order.create(1L, List.of(OrderLineItem.create(1L, 1)));
        order.changeOrderStatus(invalidOrderStatus);
        order.setOrderTableId(orderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        OrderTable 주문테이블 = orderTableDao.save(orderTable);

        TableEmptyStatusUpdateRequest request = new TableEmptyStatusUpdateRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = OrderTable.create(0, false);
        OrderTable 주문테이블 = orderTableDao.save(orderTable);

        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(1);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(주문테이블.getId(), request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, numberOfGuests가 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void changeNumberOfGuests_FailWithInvalidNumberOfGuests(int invalidNumberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.create(0, false);
        OrderTable 주문테이블 = orderTableDao.save(orderTable);

        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, orderTableId를 찾을 수 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithInvalidOrderTableId() {
        // given
        Long invalidOrderTableId = 1000L;

        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, 테이블이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithEmptyIsTrue() {
        // given
        OrderTable orderTable = OrderTable.create(0, true);
        OrderTable 주문테이블 = orderTableDao.save(orderTable);

        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
