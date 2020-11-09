package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

class OrderServiceTest implements ServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private TableService tableService;

	@DisplayName("메뉴로 주문을 등록한다")
	@Test
	void create() {
		OrderTable orderTable = tableService.create(new OrderTable());

		Order input = new Order();
		input.setOrderTableId(orderTable.getId());

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);

		input.setOrderLineItems(Arrays.asList(orderLineItem));
		Order output = orderService.create(input);

		assertThat(output.getId()).isNotNull();
	}

	@DisplayName("빈 테이블에 주문 등록시 예외가 발생한다")
	@Test
	void create_WhenEmptyTable_ThrowException() {
		OrderTable orderTable = tableService.create(new OrderTable());
		orderTable.setEmpty(true);
		tableService.changeEmpty(orderTable.getId(), orderTable);

		Order input = new Order();
		input.setOrderTableId(orderTable.getId());

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		input.setOrderLineItems(Arrays.asList(orderLineItem));

		assertThatThrownBy(() -> orderService.create(input)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 목록을 조회한다")
	@Test
	void list() {
		OrderTable orderTable = tableService.create(new OrderTable());

		Order input = new Order();
		input.setOrderTableId(orderTable.getId());

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);

		input.setOrderLineItems(Arrays.asList(orderLineItem));
		Order output = orderService.create(input);

		List<Order> orders = orderService.list();
		int lastOrderIndex = orders.size() - 1;

		assertAll(
			() -> assertThat(orders).isNotEmpty(),
			() -> assertThat(orders.get(lastOrderIndex).getId()).isEqualTo(output.getId()),
			() -> assertThat(orders.get(lastOrderIndex).getOrderedTime()).isEqualTo(output.getOrderedTime()),
			() -> assertThat(orders.get(lastOrderIndex).getOrderStatus()).isEqualTo(output.getOrderStatus()),
			() -> assertThat(orders.get(lastOrderIndex).getOrderTableId()).isEqualTo(output.getOrderTableId())
		);
	}

	@DisplayName("주문의 상태를 변경한다")
	@ValueSource(strings = {"COOKING", "MEAL"})
	@ParameterizedTest
	void changeOrderStatus(String orderStatusName) {
		OrderTable orderTable = tableService.create(new OrderTable());

		Order input = new Order();
		input.setOrderTableId(orderTable.getId());

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);

		input.setOrderLineItems(Arrays.asList(orderLineItem));
		Order output = orderService.create(input);
		output.setOrderStatus(orderStatusName);

		Order changedOutput = orderService.changeOrderStatus(output.getId(), output);

		assertThat(changedOutput.getOrderStatus()).isEqualTo(orderStatusName);
	}

	@DisplayName("주문 상태가 완료일 경우 주문 상태 변경시 예외가 발생한다")
	@Test
	void changeOrderStatus_WhenOrderStatusIsCompletion_ThrowException() {
		OrderTable orderTable = tableService.create(new OrderTable());

		Order input = new Order();
		input.setOrderTableId(orderTable.getId());

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);

		input.setOrderLineItems(Arrays.asList(orderLineItem));
		Order output = orderService.create(input);
		output.setOrderStatus("COMPLETION");

		Order changedOutput = orderService.changeOrderStatus(output.getId(), output);
		changedOutput.setOrderStatus("MEAL");

		assertThatThrownBy(() -> orderService.changeOrderStatus(changedOutput.getId(), changedOutput))
			.isInstanceOf(IllegalArgumentException.class);
	}
}