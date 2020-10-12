package kitchenpos.service;

import static kitchenpos.integrationtest.step.OrderServiceTestStep.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.service.common.ServiceTest;

@DisplayName("OrderService 단위 테스트")
class OrderServiceTest extends ServiceTest {

	@Autowired
	OrderService orderService;

	@DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
	@Test
	void create() {
		Order order = createValidOrder();

		Order result = orderService.create(order);

		assertAll(
			() -> assertThat(result.getId()).isEqualTo(6L),
			() -> assertThat(result.getOrderTableId()).isEqualTo(2L),
			() -> assertThat(result.getOrderStatus()).isEqualTo("COOKING"),
			() -> assertThat(result.getOrderedTime()).isNotNull(),
			() -> assertThat(result.getOrderLineItems()).hasSize(1)
		);
	}

	@Test
	void list() {
	}

	@Test
	void changeOrderStatus() {
	}
}