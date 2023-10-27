package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.support.ServiceTest;
import kitchenpos.table.OrderTable;
import kitchenpos.table.TableGroup;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.persistence.TableGroupRepository;
import kitchenpos.table.request.OrderTableCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderTableServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableService orderTableService;

    @Test
    void 주문테이블을_저장() {
        // given
        var request = new OrderTableCreateRequest(4, true);

        // when
        OrderTable actual = orderTableService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(4),
            () -> assertThat(actual.isEmpty()).isEqualTo(true),
            () -> assertThat(actual.getId()).isPositive()
        );
    }

    @Test
    void 모든_주문_테이블_조회() {
        // given
        List<OrderTable> expected = new ArrayList<>();
        expected.add(orderTableRepository.save(new OrderTable(3, true)));
        expected.add(orderTableRepository.save(new OrderTable(4, false)));
        expected.add(orderTableRepository.save(new OrderTable(2, true)));

        // when
        List<OrderTable> actual = orderTableService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Nested
    class 주문_테이블_상태를_변경 {

        @Test
        void 이용가능에서_사용중으로_변경() {
            // given
            Long givenId = orderTableRepository.save(new OrderTable(5, false)).getId();

            // when
            OrderTable actual = orderTableService.changeEmpty(givenId, true);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        void 사용중에서_이용가능으로_변경() {
            // given
            Long givenId = orderTableRepository.save(new OrderTable(5, true)).getId();

            // when
            OrderTable actual = orderTableService.changeEmpty(givenId, false);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 없는_주문테이블이면_예외() {
            // when && then
            Assertions.assertThatThrownBy(() -> orderTableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 특정_테이블그룹에_속한다면_예외() {
            // given
            Long tableGroupId = tableGroupRepository.save(TableGroup.createEmpty(LocalDateTime.now())).getId();
            OrderTable orderTableA = orderTableRepository.save(new OrderTable(null, tableGroupId, 3, true));
            OrderTable orderTableB = orderTableRepository.save(new OrderTable(null, tableGroupId, 2, true));

            // when && then
            Assertions.assertThatThrownBy(() -> orderTableService.changeEmpty(orderTableA.getId(), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블그룹에 속한 테이블의 상태를 변경할 수 없습니다.");

        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COMPLETION"}, mode = Mode.EXCLUDE)
        void 해당하는_주문테이블의_주문이_완료되지_않았으면_예외(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = orderTableRepository.save(new OrderTable(5, false));
            Order order = new Order(orderTable, orderStatus, List.of(new OrderLineItem(1L, 5)), LocalDateTime.now());
            orderRepository.save(order);

            // when && then
            Assertions.assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 완료된 테이블만 상태를 변경할 수 있습니다.");
        }
    }

    @Nested
    class 방문자수를_변경 {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        void 성공(int changedNumberOfGuests) {
            // given
            Long givenId = orderTableRepository.save(new OrderTable(5, false)).getId();

            // when
            OrderTable actual = orderTableService.changeNumberOfGuests(givenId, changedNumberOfGuests);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        void 변경할려는_방문자수가_음수면_예외(int wrongValue) {
            // given
            Long givenId = orderTableRepository.save(new OrderTable(5, false)).getId();

            // when & then
            Assertions.assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(givenId, wrongValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 인원은 양수여야합니다.");
        }

        @Test
        void 해당하는_주문테이블이_없으면_예외() {
            // when & then
            Assertions.assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문테이블입니다.");
        }

        @Test
        void 주문테이블이_비어있으면_예외() {
            // given
            Long givenId = orderTableRepository.save(new OrderTable(5, true)).getId();

            // when & then
            Assertions.assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(givenId, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블의 인원을 변경할 수 없습니다.");
        }
    }
}
