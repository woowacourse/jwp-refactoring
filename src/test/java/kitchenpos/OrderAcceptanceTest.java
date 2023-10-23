package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.step.OrderStep;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.NOT_EMPTY_테이블;
import static kitchenpos.step.MenuGroupStep.MENU_GROUP_REQUEST_일식;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_생성_요청하고_메뉴_그룹_반환;
import static kitchenpos.step.MenuStep.MENU_CREATE_REQUEST_스키야키;
import static kitchenpos.step.MenuStep.메뉴_생성_요청하고_아이디_반환;
import static kitchenpos.step.OrderStep.ORDER_CREATE_REQUEST;
import static kitchenpos.step.OrderStep.ORDER_UPDATE_REQUEST;
import static kitchenpos.step.OrderStep.주문_상태_변경_요청;
import static kitchenpos.step.OrderStep.주문_생성_요청;
import static kitchenpos.step.OrderStep.주문_생성_요청하고_주문_반환;
import static kitchenpos.step.OrderStep.주문_조회_요청;
import static kitchenpos.step.ProductStep.PRODUCT_CREATE_REQUEST_스키야키;
import static kitchenpos.step.ProductStep.상품_생성_요청하고_상품_반환;
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

            final MenuGroup menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(MENU_CREATE_REQUEST_스키야키(
                    BigDecimal.valueOf(11_900),
                    menuGroup.getId(),
                    List.of(menuProduct)
            ));

            final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2L);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final ExtractableResponse<Response> response = 주문_생성_요청(ORDER_CREATE_REQUEST(savedOrderTable.getId(), List.of(orderLineItem)));

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.jsonPath().getLong("orderTableId")).isEqualTo(savedOrderTable.getId()),
                    () -> assertThat(response.jsonPath().getList("orderLineItems", OrderLineItem.class))
                            .usingRecursiveComparison()
                            .comparingOnlyFields("menuId")
                            .isEqualTo(order.getOrderLineItems())
            );
        }

        @Test
        void 주문_항목은_반드시_1개_이상이어야_한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final ExtractableResponse<Response> response = 주문_생성_요청(ORDER_CREATE_REQUEST(savedOrderTable.getId(), List.of()));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 동일한_메뉴는_1개의_주문항목으로_표시되어야_한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(MENU_CREATE_REQUEST_스키야키(
                    BigDecimal.valueOf(11_900),
                    menuGroup.getId(),
                    List.of(menuProduct)
            ));

            final OrderLineItem orderLineItem1 = new OrderLineItem(menuId, 1L);
            final OrderLineItem orderLineItem2 = new OrderLineItem(menuId, 1L);

            final ExtractableResponse<Response> response = 주문_생성_요청(ORDER_CREATE_REQUEST(savedOrderTable.getId(), List.of(orderLineItem1, orderLineItem2)));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 주문을_생성하려면_주문하는_테이블이_존재해야_한다() {
            final MenuGroup menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(MENU_CREATE_REQUEST_스키야키(
                    BigDecimal.valueOf(11_900),
                    menuGroup.getId(),
                    List.of(menuProduct)
            ));

            final OrderLineItem orderLineItem = new OrderLineItem(menuId, 1L);

            final Order order = new Order();
            order.setOrderLineItems(List.of(orderLineItem));

            final ExtractableResponse<Response> response = 주문_생성_요청(ORDER_CREATE_REQUEST(1L, List.of(orderLineItem)));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class OrderQueryTest {

        @Test
        void 주문을_조회한다() {
            final OrderTable orderTable = NOT_EMPTY_테이블();
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

            final MenuGroup menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(MENU_CREATE_REQUEST_스키야키(
                    BigDecimal.valueOf(11_900),
                    menuGroup.getId(),
                    List.of(menuProduct)
            ));

            final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2L);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order savedOrder = 주문_생성_요청하고_주문_반환(OrderStep.toRequest(order));

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

            final MenuGroup menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(MENU_CREATE_REQUEST_스키야키(
                    BigDecimal.valueOf(11_900),
                    menuGroup.getId(),
                    List.of(menuProduct)
            ));

            final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2L);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order savedOrder = 주문_생성_요청하고_주문_반환(OrderStep.toRequest(order));
            savedOrder.setOrderStatus("COOKING");

            final ExtractableResponse<Response> response = 주문_상태_변경_요청(savedOrder.getId(), ORDER_UPDATE_REQUEST("COOKING"));
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

            final MenuGroup menuGroup = 메뉴_그룹_생성_요청하고_메뉴_그룹_반환(MENU_GROUP_REQUEST_일식);
            final Product product = 상품_생성_요청하고_상품_반환(PRODUCT_CREATE_REQUEST_스키야키);

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1L);

            final Long menuId = 메뉴_생성_요청하고_아이디_반환(MENU_CREATE_REQUEST_스키야키(
                    BigDecimal.valueOf(11_900),
                    menuGroup.getId(),
                    List.of(menuProduct)
            ));

            final OrderLineItem orderLineItem = new OrderLineItem(menuId, 2L);

            final Order order = new Order();
            order.setOrderTableId(savedOrderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            final Order savedOrder = 주문_생성_요청하고_주문_반환(OrderStep.toRequest(order));
            savedOrder.setOrderStatus("COMPLETION");

            주문_상태_변경_요청(savedOrder.getId(), ORDER_UPDATE_REQUEST("COMPLETION"));

            savedOrder.setOrderStatus("COOKING");
            final ExtractableResponse<Response> response = 주문_상태_변경_요청(savedOrder.getId(), ORDER_UPDATE_REQUEST("COMPLETION"));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }
}
