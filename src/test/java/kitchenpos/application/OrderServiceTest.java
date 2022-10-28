package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixtures.TestFixtures.메뉴_그룹_생성;
import static kitchenpos.fixtures.TestFixtures.메뉴_생성;
import static kitchenpos.fixtures.TestFixtures.주문_생성;
import static kitchenpos.fixtures.TestFixtures.주문_생성_요청;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static kitchenpos.fixtures.TestFixtures.주문_항목_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_주문이_입력되면 extends ServiceTest {

            private final MenuGroup menuGroup = 메뉴_그룹_생성("한마리메뉴");
            private final Menu menu = 메뉴_생성("치킨", BigDecimal.valueOf(1_000L), 1L, new ArrayList<>());
            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);
            private final OrderLineItemRequest orderLineItemRequest = 주문_항목_요청(1L, 1L);
            private final OrderCreateRequest request = 주문_생성_요청(1L, List.of(orderLineItemRequest));

            @BeforeEach
            void setUp() {
                menuGroupRepository.save(menuGroup);
                menuRepository.save(menu);
                orderTableRepository.save(orderTable);
            }

            @Test
            void 해당_주문을_반환한다() {
                final OrderResponse response = orderService.create(request);

                assertAll(
                        () -> assertThat(response).isNotNull(),
                        () -> assertThat(response.getOrderStatus()).isEqualTo(COOKING.name())
                );
            }
        }

        @Nested
        class 빈_주문_항목으로_주문이_입력되면 extends ServiceTest {

            private final OrderCreateRequest request = 주문_생성_요청(null, Collections.emptyList());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 존재하지_않는_메뉴로_주문이_입력되면 extends ServiceTest {

            private final OrderLineItemRequest orderLineItemRequest = 주문_항목_요청(1L, 1L);
            private final OrderCreateRequest request = 주문_생성_요청(null, List.of(orderLineItemRequest));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 없는_주문_테이블로_주문을_입력하면 extends ServiceTest {

            private final MenuGroup menuGroup = 메뉴_그룹_생성("한마리메뉴");
            private final Menu menu = 메뉴_생성("치킨", BigDecimal.valueOf(1_000L), 1L, new ArrayList<>());
            private final OrderLineItemRequest orderLineItemRequest = 주문_항목_요청(1L, 1L);
            private final OrderCreateRequest request = 주문_생성_요청(0L, List.of(orderLineItemRequest));


            @BeforeEach
            void setUp() {
                menuGroupRepository.save(menuGroup);
                menuRepository.save(menu);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 비어있는_주문_테이블로_주문을_입력하면 extends ServiceTest {

            private final MenuGroup menuGroup = 메뉴_그룹_생성("한마리메뉴");
            private final Menu menu = 메뉴_생성("치킨", BigDecimal.valueOf(1_000L), 1L, new ArrayList<>());
            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);
            private final OrderLineItemRequest orderLineItemRequest = 주문_항목_요청(1L, 1L);
            private final OrderCreateRequest request = 주문_생성_요청(1L, List.of(orderLineItemRequest));

            @BeforeEach
            void setUp() {
                menuGroupRepository.save(menuGroup);
                menuRepository.save(menu);
                orderTableRepository.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 extends ServiceTest {

            @Test
            void 모든_주문을_반환한다() {
                final List<OrderResponse> orders = orderService.list();
                assertThat(orders).isEmpty();
            }
        }
    }

    @Nested
    class changeOrderStatus_메서드는 {

        @Nested
        class 주문_id와_주문이_입력되면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);
            private final Order order = 주문_생성(orderTable, MEAL, LocalDateTime.now(), Collections.emptyList());
            final OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest("COMPLETION");

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable);
                orderRepository.save(order);
            }

            @Test
            void 주문상태를_변경하고_주문을_반환한다() {
                final OrderResponse response = orderService.changeOrderStatus(1L, updateRequest);

                assertThat(response.getOrderStatus()).isEqualTo(updateRequest.getOrderStatus());
            }
        }
    }
}
