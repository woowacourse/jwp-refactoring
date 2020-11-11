package kitchenpos.acceptance.order;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.menu.MenuAcceptanceStep;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptanceStep;
import kitchenpos.acceptance.ordertable.OrderTableAcceptanceStep;
import kitchenpos.acceptance.product.ProductAcceptanceStep;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class OrderAcceptanceStep {

	public static Order create(String productName, int productPrice, String menuGroupName, String menuName,
		int menuPrice) {
		OrderTable orderTable = OrderTableAcceptanceStep.getPersist();

		Order order = new Order();
		order.setOrderTableId(orderTable.getId());

		Product product = ProductAcceptanceStep.getPersist(productName, productPrice);
		MenuGroup menuGroup = MenuGroupAcceptanceStep.getPersist(menuGroupName);

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(product.getId());
		menuProduct.setQuantity(1L);

		Menu persist = MenuAcceptanceStep.getPersist(menuName, menuPrice, menuGroup, menuProduct);

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(persist.getId());

		order.setOrderLineItems(Collections.singletonList(orderLineItem));
		return order;
	}

	public static ExtractableResponse<Response> requestToCreateOrder(Order order) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(order)
			.when()
			.post("/api/orders")
			.then().log().all()
			.extract();
	}

	public static void assertThatCreateOrder(ExtractableResponse<Response> response) {
		Order actual = response.jsonPath().getObject(".", Order.class);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
			() -> assertThat(actual.getOrderedTime()).isNotNull()
		);
	}

	public static Order getPersist(String productName, int productPrice, String menuGroupName, String menuName,
		int menuPrice) {
		Order order = create(productName, productPrice, menuGroupName, menuName, menuPrice);
		ExtractableResponse<Response> response = requestToCreateOrder(order);
		return response.jsonPath().getObject(".", Order.class);
	}

	public static ExtractableResponse<Response> requestToFindOrders() {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/orders")
			.then().log().all()
			.extract();
	}

	public static void assertThatFindOrders(ExtractableResponse<Response> response, List<Order> expected) {
		List<Order> actual = response.jsonPath().getList(".", Order.class);

		assertAll(
			() -> assertThat(actual).usingElementComparatorOnFields("id").isNotNull(),
			() -> assertThat(actual).usingElementComparatorOnFields("orderedTime").isEqualTo(expected),
			() -> assertThat(actual).usingElementComparatorOnFields("orderLineItems").isNotNull()
		);
	}

	public static ExtractableResponse<Response> requestToFindOrders(Order order) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(order)
			.when()
			.put("/api/orders/" + order.getId() + "/order-status")
			.then().log().all()
			.extract();
	}

	public static void assertThatChangeOrderStatus(ExtractableResponse<Response> response, Order expected) {
		Order actual = response.jsonPath().getObject(".", Order.class);

		assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
	}
}
