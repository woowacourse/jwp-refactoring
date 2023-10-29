package kitchenpos.core.domain.order;

import static kitchenpos.core.domain.vo.OrderStatus.COOKING;
import static kitchenpos.core.domain.vo.OrderStatus.MEAL;
import static kitchenpos.core.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.core.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.core.domain.order.repository.OrderRepository;
import kitchenpos.core.domain.ordertable.OrderTable;
import kitchenpos.core.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderValidator orderValidator = new OrderValidator(orderRepository);

    @Test
    @DisplayName("빈 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_빈_테이블() {
        // given
        final OrderTable emptyTable = 빈_테이블_생성();

        // expected
        assertThatThrownBy(() -> emptyTable.validateToPlace())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블의 방문한 손님 수는 0 이상이어야 한다.")
    void 주문_테이블_방문한_손님_수_변경_실패_음수() {
        // given
        final OrderTable table = 주문_테이블_생성();

        // expected
        assertThatThrownBy(() -> table.changeNumberOfGuests(-10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 방문한 손님 수를 변경할 수 없다.")
    void 주문_테이블_방문한_손님_수_변경_실패_빈_테이블() {
        // given
        final OrderTable table = 빈_테이블_생성();

        // expected
        assertThatThrownBy(() -> table.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소속된 단체가 있으면 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_단체_소속() {
        // given
        final OrderTable tableInGroup = 빈_테이블_생성();
        new TableGroup(1L).addOrderTables(List.of(tableInGroup, 빈_테이블_생성()));

        // expected
        assertThatThrownBy(() -> tableInGroup.changeEmpty(orderValidator, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 또는 식사 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_식사_상태() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();

        // when
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(COOKING, MEAL)))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(orderValidator, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소속된 단체가 없는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_이미_소속_단체가_있는_테이블() {
        // given
        final List<OrderTable> tablesInGroup = List.of(빈_테이블_생성(), 빈_테이블_생성());

        // when
        new TableGroup(1L).addOrderTables(tablesInGroup);

        // then
        assertThatThrownBy(() -> new TableGroup(2L).addOrderTables(tablesInGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_주문_테이블() {
        // given
        final List<OrderTable> tablesInGroup = List.of(주문_테이블_생성(), 빈_테이블_생성());

        // when
        final TableGroup tableGroup = new TableGroup(1L);

        // then
        assertThatThrownBy(() -> tableGroup.addOrderTables(tablesInGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 개별 테이블인 테이블을 단체에서 분할할 수 없다.")
    void 단체_테이블_분할_실패_개별_테이블() {
        assertThatThrownBy(() -> 빈_테이블_생성().unGroup(orderValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("이미 조리 또는 식사 중인 테이블이 존재한다면 단체를 분할할 수 없다.")
    void 단체_테이블_분할_실패_주문_상태(String orderStatus) {
        // given
        final OrderTable emptyTable = 빈_테이블_생성();
        emptyTable.group(1L);

        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(emptyTable.getId(), List.of(COOKING, MEAL)))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> emptyTable.unGroup(orderValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
