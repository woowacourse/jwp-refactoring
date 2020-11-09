package kitchenpos.acceptance.order;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceStep;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

	@DisplayName("주문을 생성한다")
	@Test
	void create() {
		// given
		Order order = OrderAcceptanceStep.create("방어회", 32_000, "겨울 회", "방어회 (소)", 24_000);

		// when
		ExtractableResponse<Response> response = OrderAcceptanceStep.requestToCreateOrder(order);

		// then
		AcceptanceStep.assertThatStatusIsCreated(response);
		OrderAcceptanceStep.assertThatCreateOrder(response, order);
	}

	@DisplayName("모든 주문을 조회한다")
	@Test
	void list() {
		// given
		Order order = OrderAcceptanceStep.getPersist("방어회", 32_000, "겨울 회", "방어회 (소)", 24_000);
		Order order2 = OrderAcceptanceStep.getPersist("방어회", 32_000, "겨울 회", "방어회 (중)", 32_000);

		// when
		ExtractableResponse<Response> response = OrderAcceptanceStep.requestToFindOrders();

		// then
		AcceptanceStep.assertThatStatusIsOk(response);
		OrderAcceptanceStep.assertThatFindOrders(response, Arrays.asList(order, order2));
	}

	@DisplayName("주문 상태를 변경한다")
	@Test
	void changeOrderStatus() {
		// given
		Order order = OrderAcceptanceStep.getPersist("방어회", 32_000, "겨울 회", "방어회 (소)", 24_000);
		order.setOrderStatus(OrderStatus.COOKING.name());

		// when
		ExtractableResponse<Response> response = OrderAcceptanceStep.requestToFindOrders(order);

		//then
		AcceptanceStep.assertThatStatusIsOk(response);
		OrderAcceptanceStep.assertThatChangeOrderStatus(response, order);
	}
}
