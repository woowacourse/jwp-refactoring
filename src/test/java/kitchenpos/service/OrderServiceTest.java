package kitchenpos.service;

import static kitchenpos.integrationtest.step.OrderServiceTestStep.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.service.common.ServiceTest;

@DisplayName("OrderService 단위 테스트")
class OrderServiceTest extends ServiceTest {

	@Autowired
	private OrderService orderService;

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

	@DisplayName("빈 테이블에는 주문을 등록할 수 없다.")
	@Test
	void create_WhenTableIsEmpty() {
		Order order = createOrderThatTableIsEmpty();

		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문의 목록을 조회할 수 있다.")
	@Test
	void list() {
		List<Order> list = orderService.list();

		assertThat(list).hasSize(5);
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@ParameterizedTest
	@CsvSource(value = {"1,MEAL", "2,COOKING"})
	void changeOrderStatus(long id, String orderStatus) {
		Order order = new Order();
		order.setOrderStatus(orderStatus);

		Order result = orderService.changeOrderStatus(id, order);

		assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
	}

	@DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
	@ParameterizedTest
	@CsvSource(value = {"3,MEAL", "3,COOKING"})
	void changeOrderStatus_WhenOrderStatusIsCompletion(long id, String orderStatus) {
		Order order = new Order();
		order.setOrderStatus(orderStatus);

		assertThatThrownBy(() -> orderService.changeOrderStatus(id, order))
			.isInstanceOf(IllegalArgumentException.class);
	}
}