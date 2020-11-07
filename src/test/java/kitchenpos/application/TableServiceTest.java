package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

class TableServiceTest implements ServiceTest {

	@Autowired
	private TableService tableService;

	@Autowired
	private OrderService orderService;

	@DisplayName("주문 테이블을 등록한다")
	@Test
	void create() {
		OrderTable output = tableService.create(new OrderTable());

		assertThat(output.getId()).isNotNull();
	}

	@DisplayName("주문 테이블을 조회한다")
	@Test
	void list() {
		assertThat(tableService.list()).isNotEmpty();
	}

	@DisplayName("주문테이블에 빈 테이블을 설정 또는 해지한다")
	@ValueSource(booleans = {true, false})
	@ParameterizedTest
	void changeEmpty(boolean isEmpty) {
		OrderTable output = tableService.create(new OrderTable());
		output.setEmpty(isEmpty);
		OrderTable changedTable = tableService.changeEmpty(output.getId(), output);

		assertThat(changedTable.isEmpty()).isEqualTo(isEmpty);
	}

	@DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지시 예외가 발생한다")
	@ValueSource(strings = {"COOKING", "MEAL"})
	@ParameterizedTest
	void changeEmpty_WhenOrderStatusCookingOrMeal_ThrowException(String invalidOderStatus) {
		OrderTable output = tableService.create(new OrderTable());

		Order order = new Order();
		order.setOrderTableId(output.getId());
		order.setOrderStatus(invalidOderStatus);

		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);

		order.setOrderLineItems(Arrays.asList(orderLineItem));
		orderService.create(order);

		assertThatThrownBy(() -> tableService.changeEmpty(output.getId(), output)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("방문한 손님 수를 입력한다")
	@ValueSource(ints = {0, 1, 100})
	@ParameterizedTest
	void changeNumberOfGuests(int guestCount) {
		OrderTable output = tableService.create(new OrderTable());
		output.setNumberOfGuests(guestCount);
		OrderTable changedTable = tableService.changeNumberOfGuests(output.getId(), output);

		assertThat(changedTable.getNumberOfGuests()).isEqualTo(guestCount);
	}

	@DisplayName("방문한 손님 수가 0명 미만일 경우 예외가 발생한다")
	@ValueSource(ints = {-1, -2, -100})
	@ParameterizedTest
	void changeNumberOfGuests_WhenUnderOne_ThrowException(int invalidGuestCount) {
		OrderTable output = tableService.create(new OrderTable());
		output.setNumberOfGuests(invalidGuestCount);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(output.getId(), output)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블에 손님 수를 입력하려는 경우 예외가 발생한다")
	@Test
	void changeNumberOfGuests_WhenEmptyTable_ThrowException() {
		OrderTable output = tableService.create(new OrderTable());
		output.setEmpty(true);
		tableService.changeEmpty(output.getId(), output);

		output.setNumberOfGuests(3);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(output.getId(), output)).isInstanceOf(IllegalArgumentException.class);
	}
}