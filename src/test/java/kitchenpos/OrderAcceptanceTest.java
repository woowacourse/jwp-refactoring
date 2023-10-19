package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.일식;
import static kitchenpos.fixture.OrderTableFixture.NOT_EMPTY_테이블;
import static kitchenpos.fixture.ProductFixture.스키야키;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_생성_요청하고_아이디_반환;
import static kitchenpos.step.MenuStep.메뉴_생성_요청하고_아이디_반환;
import static kitchenpos.step.OrderStep.주문_상태_변경_요청;
import static kitchenpos.step.OrderStep.주문_생성_요청;
import static kitchenpos.step.OrderStep.주문_생성_요청하고_주문_반환;
import static kitchenpos.step.OrderStep.주문_조회_요청;
import static kitchenpos.step.ProductStep.상품_생성_요청하고_아이디_반환;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_테이블_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Nested
    class OrderCreateTest {

        @Test
        void 주문을_생성한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(menu);

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setQuantity(2L);
            orderLineItem.setMenuId(menuId);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final ExtractableResponse<Response> response = 주문_생성_요청(order);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.jsonPath().getLong("orderTableId")).isEqualTo(savedOrderTable.getId()),
                    () -> assertThat(response.jsonPath().getList("orderLineItems", OrderLineItem.class))
                            .usingRecursiveComparison()
                            .comparingOnlyFields("menuId", "quantity")
                            .isEqualTo(order.getOrderLineItems())
            );
        }

        @Test
        void 주문_항목은_반드시_1개_이상이어야_한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of());

            final ExtractableResponse<Response> response = 주문_생성_요청(order);

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 동일한_메뉴는_1개의_주문항목으로_표시되어야_한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(menu);

            final OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setQuantity(1L);
            orderLineItem1.setMenuId(menuId);

            final OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setQuantity(1L);
            orderLineItem2.setMenuId(menuId);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

            final ExtractableResponse<Response> response = 주문_생성_요청(order);

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 주문을_생성하려면_주문하는_테이블이_존재해야_한다() {
            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(menu);

            final OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setQuantity(1L);
            orderLineItem1.setMenuId(menuId);

            final Order order = new Order();
            order.setOrderLineItems(List.of(orderLineItem1));

            final ExtractableResponse<Response> response = 주문_생성_요청(order);

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class OrderQueryTest {

        @Test
        void 주문을_조회한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(menu);

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setQuantity(2L);
            orderLineItem.setMenuId(menuId);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order savedOrder = 주문_생성_요청하고_주문_반환(order);

            final ExtractableResponse<Response> response = 주문_조회_요청();
            final List<Order> result = response.jsonPath().getList("", Order.class);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(result).hasSize(1),
                    () -> assertThat(result.get(0))
                            .usingRecursiveComparison()
                            .isEqualTo(savedOrder)
            );
        }
    }

    @Nested
    class QueryUpdateTest {

        @Test
        void 주문_상태를_변경한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(menu);

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setQuantity(2L);
            orderLineItem.setMenuId(menuId);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order savedOrder = 주문_생성_요청하고_주문_반환(order);
            savedOrder.setOrderStatus("COOKING");

            final ExtractableResponse<Response> response = 주문_상태_변경_요청(savedOrder);
            final Order result = response.jsonPath().getObject("", Order.class);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(result)
                            .usingRecursiveComparison()
                            .isEqualTo(savedOrder)
            );
        }

        @Test
        void 주문_상태가_COMPLETION인_테이블_상태는_변경할_수_없다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(menuGroup);
            final Long productId = 상품_생성_요청하고_아이디_반환(스키야키());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(menu);

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setQuantity(2L);
            orderLineItem.setMenuId(menuId);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order savedOrder = 주문_생성_요청하고_주문_반환(order);
            savedOrder.setOrderStatus("COMPLETION");

            주문_상태_변경_요청(savedOrder);

            savedOrder.setOrderStatus("COOKING");
            final ExtractableResponse<Response> response = 주문_상태_변경_요청(savedOrder);

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }
}
