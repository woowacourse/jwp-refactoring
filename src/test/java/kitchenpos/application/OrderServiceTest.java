package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderUpdateRequest;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    @Nested
    class 주문_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when
            final OrderResponse extract = orderService.create(new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1))));

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청시_주문할_주문_아이템을_입력하지_않으면_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when & then
            assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(orderTable.getId(), null)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청시_등록되지_않은_메뉴로_주문_아이템을_입력하면_예외가_발생한다() {
            // given
            menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            productService.create(new ProductCreateRequest("짜장면", 1000));
            final Long notRegisterMenuId = 100L;
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when & then
            assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(new OrderLineItemRequest(notRegisterMenuId, 1)))))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청시_존재하지_않는_주문_테이블로_요청하는_경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final Long notRegisterOrderTableId = 100L;

            // when & then
            assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(notRegisterOrderTableId, List.of(new OrderLineItemRequest(menu.getId(), 1)))))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_리스트_조회 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final OrderResponse order = orderService.create(new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1))));

            // when
            final List<OrderResponse> extract = orderService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }

    @Nested
    class 주문_상태_변경 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final OrderResponse order = orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(new OrderLineItemRequest(menu.getId(), 1))));

            // when
            final OrderResponse extract = orderService.changeOrderStatus(order.getId(), new OrderUpdateRequest(OrderStatus.MEAL.name()));

            // then
            assertThat(extract.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 요청시_주문이_완료_상태이면_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final OrderResponse order =  orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(new OrderLineItemRequest(menu.getId(), 1))));
            orderService.changeOrderStatus(order.getId(), new OrderUpdateRequest(OrderStatus.COMPLETION.name()));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderUpdateRequest(OrderStatus.MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청시_존재하지_않는_주문_상태로_변경_요청하는_경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final OrderResponse order =  orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(new OrderLineItemRequest(menu.getId(), 1))));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderUpdateRequest("Not Registered OrderStatus")))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
