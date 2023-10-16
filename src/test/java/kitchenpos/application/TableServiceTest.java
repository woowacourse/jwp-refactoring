package kitchenpos.application;

import kitchenpos.application.dto.OrderTableEmptyRequest;
import kitchenpos.application.dto.OrderTableNumberOfGuestRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.fake.InMemoryOrderRepository;
import kitchenpos.fake.InMemoryOrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static kitchenpos.fixture.OrderTableFixtrue.orderTableNumberOfGuestsRequest;
import static kitchenpos.fixture.OrderTableFixtrue.orderTableRequest;
import static kitchenpos.fixture.TableGroupFixture.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    private TableService tableService;
    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void before() {
        orderRepository = new InMemoryOrderRepository();
        orderTableRepository = new InMemoryOrderTableRepository();
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTableRequest request = orderTableRequest(10, false);

        // when
        OrderTable savedOrderTable = tableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable).usingRecursiveComparison()
                    .ignoringFields("id", "tableGroupId")
                    .isEqualTo(orderTable(10, false));
            softly.assertThat(savedOrderTable.getId()).isNotNull();
            softly.assertThat(savedOrderTable.getTableGroup()).isNull();
        });
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));
        Order order = order(orderTable, OrderStatus.COMPLETION, List.of(orderLineItem(1L, 10)));
        orderRepository.save(order);

        // when
        OrderTable emptyTable = tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyRequest(true));

        // then
        assertThat(emptyTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_때_단체_지정이_있으면_예외가_발생한다() {
        // given
        TableGroup tableGroup = tableGroup(List.of(orderTable(10, true), orderTable(11, true)));
        OrderTable orderTable = orderTableRepository.save(orderTable(tableGroup, 10, false));

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 테이블은 변경할 수 없습니다");
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 주문_테이블을_빈_테이블로_변경할_때_주문_상태가_완료가_아니면_예외가_발생한다(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));
        Order order = order(orderTable, orderStatus, List.of(orderLineItem(1L, 10)));
        orderRepository.save(order);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 완료가 아닙니다");
    }

    @ValueSource(ints = {-1, -2})
    @ParameterizedTest
    void 주문_테이블의_손님_수를_변경할_때_0보다_작으면_예외가_발생한다(int numberOfGuests) {
        // given
        OrderTableNumberOfGuestRequest request = orderTableNumberOfGuestsRequest(numberOfGuests);
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블의 손님 수가 0보다 커야합니다");
    }

    @Test
    void 주문_테이블의_손님_수를_변경할_때_저장되어있는_주문_테이블이_없으면_예외가_발생한다() {
        // given
        OrderTableNumberOfGuestRequest request = orderTableNumberOfGuestsRequest(10);
        Long invalidOrderTableId = Long.MAX_VALUE;

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 없습니다");
    }

    @Test
    void 주문_테이블의_손님_수를_변경할_때_주문_테이블이_빈_테이블이면_예외가_발생한다() {
        // given
        OrderTableNumberOfGuestRequest request = orderTableNumberOfGuestsRequest(11);
        OrderTable orderTable = orderTableRepository.save(orderTable(10, true));

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인원을 변경할 테이블은 빈 테이블일 수 없습니다");
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        OrderTableNumberOfGuestRequest request = orderTableNumberOfGuestsRequest(11);
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));

        // when
        OrderTable updatedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), request);

        // then
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(11);
    }
}
