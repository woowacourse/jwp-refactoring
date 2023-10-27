package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableValidatorImpl;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyStatusUpdateRequest;
import kitchenpos.table.dto.TableNumberOfGuestsUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({TableService.class, OrderTableValidatorImpl.class})
class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableValidator orderTableValidator;


    @DisplayName("테이블을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        TableCreateRequest request = new TableCreateRequest(0, true);

        // when
        OrderTable actual = tableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTableRepository.findById(actual.getId())).isPresent();
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }


    @DisplayName("테이블 empty 상태 변경 시, 테이블 그룹 ID가 null이 아닌 경우 예외가 발생한다.")
    @Test
    void changeEmpty_FailWithInvalidTableGroupId() {
        // given
        OrderTable orderTable1 = OrderTable.create(0, true);
        OrderTable orderTable2 = OrderTable.create(0, true);

        TableGroup tableGroup = TableGroup.createWithGrouping(List.of(orderTable1, orderTable2));
        TableGroup 테이블그룹 = tableGroupRepository.save(tableGroup);

        orderTable1.setTableGroup(테이블그룹);
        OrderTable 주문테이블 = orderTableRepository.save(orderTable1);

        TableEmptyStatusUpdateRequest request = new TableEmptyStatusUpdateRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 지정된 테이블은 빈 테이블로 변경할 수 없습니다.");
    }

    @DisplayName("테이블 empty 상태 변경 시, 주문의 상태가 COOKING 또는 MEAL인 경우 예외가 발생한다.")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmpty_FailWithInvalidOrderStatus(OrderStatus invalidOrderStatus) {
        // given
        OrderTable orderTable = OrderTable.create(0, false);
        orderTableRepository.save(orderTable);

        Order order = Order.create(orderTable);
        order.changeOrderStatus(invalidOrderStatus);
        orderRepository.save(order);

        OrderTable 주문테이블 = orderTableRepository.save(orderTable);

        TableEmptyStatusUpdateRequest request = new TableEmptyStatusUpdateRequest(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중 또는 식사중인 주문 테이블은 빈 테이블로 변경할 수 없습니다.");
    }

    @DisplayName("테이블의 numberOfGuests를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = OrderTable.create(0, false);
        OrderTable 주문테이블 = orderTableRepository.save(orderTable);

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
        OrderTable 주문테이블 = orderTableRepository.save(orderTable);

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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("테이블의 numberOfGuests 변경 시, 테이블이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_FailWithEmptyIsTrue() {
        // given
        OrderTable orderTable = OrderTable.create(0, true);
        OrderTable 주문테이블 = orderTableRepository.save(orderTable);

        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블의 방문한 손님 수는 변경할 수 없습니다.");
    }
}
