package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class OrderServiceTest extends IntegrationTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void 주문_항목이_null이면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 주문_항목이_없으면_예외가_발생한다() {
        // given
        order.setOrderLineItems(List.of());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 존재하지_않는_메뉴를_주문하면_예외가_발생한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 메뉴가_있는경우 {

        @BeforeEach
        void setUp() {
            Menu menu = 맛있는_메뉴();
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            order.setOrderLineItems(List.of(orderLineItem));
        }

        @Test
        void 존재하지_않는_주문테이블로_주문하면_예외가_발생한다() {
            // given
            order.setOrderTableId(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTableDao.save(orderTable);
            order.setOrderTableId(orderTable.getId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Nested
        class 주문_테이블이_비어있지_않은_경우 {

            @BeforeEach
            void setUp() {
                OrderTable orderTable = new OrderTable();
                orderTable.setEmpty(false);
                OrderTable savedOrderTable = orderTableDao.save(orderTable);
                order.setOrderTableId(savedOrderTable.getId());
            }

            @Test
            void 주문을_저장한다() {
                // when
                Order result = orderService.create(order);

                // then
                assertAll(
                        () -> assertThat(result.getId()).isPositive(),
                        () -> assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(result.getId())
                );
            }
        }
    }
}
