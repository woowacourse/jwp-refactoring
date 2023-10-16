package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("세트")).getId();
        menu = menuRepository.save(new Menu("족발", BigDecimal.valueOf(1000.00), menuGroupId, null));
    }

    @Nested
    class 메뉴_생성 {

        @Test
        void 성공() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, false)).getId();
            Order order = new Order(orderTableId, COMPLETION, null, List.of(new OrderLineItem(menu.getId(), 2)));

            // when
            Order actual = orderService.create(order);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderLineItems())
                    .allSatisfy(it -> assertThat(it.getSeq()).isPositive())
            );
        }

        @Test
        void 오더라인_아이템이_없으면_예외() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, false)).getId();
            Order order = new Order(
                orderTableId,
                COMPLETION,
                null,
                Collections.emptyList());

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문한_메뉴가_없는_메뉴가_있으면_예외() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, false)).getId();
            Order order = new Order(
                orderTableId,
                COMPLETION,
                null,
                Arrays.asList(new OrderLineItem(menu.getId(), 2), new OrderLineItem(100L, 3)));

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문테이블이_비어있으면_예외() {
            // given
            Long orderTableId = orderTableRepository.save(new OrderTable(5, true)).getId();
            Order order = new Order(
                orderTableId,
                COMPLETION,
                null,
                Arrays.asList(new OrderLineItem(menu.getId(), 2)));

            // when && then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        List<Order> expected = new ArrayList<>();
        Long orderTableIdA = orderTableRepository.save(new OrderTable(5, true)).getId();
        expected.add(orderRepository.save(new Order(orderTableIdA, COOKING, Collections.emptyList())));

        Long orderTableIdB = orderTableRepository.save(new OrderTable(5, true)).getId();
        expected.add(orderRepository.save(new Order(orderTableIdB, COOKING, Collections.emptyList())));

        Long orderTableIdC = orderTableRepository.save(new OrderTable(5, true)).getId();
        expected.add(orderRepository.save(new Order(orderTableIdC, COOKING, Collections.emptyList())));

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Nested
    class 주문_상태_변경 {

        @ParameterizedTest
        @CsvSource(value = {"COOKING : MEAL", "MEAL : COOKING"}, delimiter = ':')
        void 변경_성공(OrderStatus originStatus, OrderStatus changedStatus) {
            // given
            Long orderTableIdA = orderTableRepository.save(new OrderTable(5, true)).getId();
            Long orderId = orderRepository.save(new Order(orderTableIdA, originStatus, Collections.emptyList()))
                .getId();

            // when
            Order actual = orderService.changeOrderStatus(orderId, changedStatus);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(changedStatus);
        }

        @Test
        void 바꿀려는_주문의_상태가_완료면_예외() {
            // given
            Long orderTableIdA = orderTableRepository.save(new OrderTable(5, true)).getId();
            Long orderId = orderRepository.save(new Order(orderTableIdA, COMPLETION, Collections.emptyList()))
                .getId();

            // when && then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, COOKING))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
