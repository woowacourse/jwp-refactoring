package kitchenpos.acceptance.ordertable;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceStep;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.OrderTable;

public class OrderTableAcceptanceTest extends AcceptanceTest {

	@DisplayName("주문 테이블을 생성한다")
	@Test
	void create() {
		// given
		OrderTable orderTable = new OrderTable();

		// when
		ExtractableResponse<Response> response = OrderTableAcceptanceStep.requestToCreateOrderTable(orderTable);

		// then
		AcceptanceStep.assertThatStatusIsCreated(response);
		OrderTableAcceptanceStep.assertThatCreateOrderTable(response, orderTable);
	}

	@DisplayName("모든 주문 테이블을 조회한다")
	@Test
	void list() {
		// given
		OrderTable orderTable = new OrderTable();
		OrderTable orderTable2 = new OrderTable();

		OrderTableAcceptanceStep.requestToCreateOrderTable(orderTable);
		OrderTableAcceptanceStep.requestToCreateOrderTable(orderTable2);

		// when
		ExtractableResponse<Response> response = OrderTableAcceptanceStep.requestToFindOrderTables();

		// then
		AcceptanceStep.assertThatStatusIsOk(response);
		OrderTableAcceptanceStep.assertThatFindOrderTables(response, Arrays.asList(orderTable, orderTable2));
	}

	@DisplayName("주문 테이블을 비우고 채운다")
	@ValueSource(booleans = {true, false})
	@ParameterizedTest
	void changeEmpty(boolean isEmpty) {
		// given
		OrderTable orderTable = OrderTableAcceptanceStep.getPersist();
		orderTable.setEmpty(isEmpty);

		// when
		ExtractableResponse<Response> response = OrderTableAcceptanceStep.requestToChangeEmpty(orderTable);

		// then
		AcceptanceStep.assertThatStatusIsOk(response);
		OrderTableAcceptanceStep.assertThatChangeEmpty(response, isEmpty);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = OrderTableAcceptanceStep.getPersist();
		orderTable.setNumberOfGuests(3);

		// when
		ExtractableResponse<Response> response = OrderTableAcceptanceStep.requestToChangeNumberOfGuests(orderTable);

		// then
		AcceptanceStep.assertThatStatusIsOk(response);
		OrderTableAcceptanceStep.assertThatChangeNumberOfGuests(response, 3);
	}
}
