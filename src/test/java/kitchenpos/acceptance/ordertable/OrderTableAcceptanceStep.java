package kitchenpos.acceptance.ordertable;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;

public class OrderTableAcceptanceStep {

	public static ExtractableResponse<Response> requestToCreateOrderTable(OrderTable orderTable) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when()
			.post("/api/tables")
			.then().log().all()
			.extract();
	}

	public static void assertThatCreateOrderTable(ExtractableResponse<Response> response, OrderTable expected) {
		OrderTable actual = response.jsonPath().getObject(".", OrderTable.class);

		assertThat(actual.getId()).isNotNull();
	}

	public static ExtractableResponse<Response> requestToFindOrderTables() {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/tables")
			.then().log().all()
			.extract();
	}

	public static void assertThatFindOrderTables(ExtractableResponse<Response> response, List<OrderTable> expected) {
		List<OrderTable> actual = response.jsonPath().getList(".", OrderTable.class);

		assertThat(actual).usingElementComparatorOnFields("id").isNotNull();
	}

	public static OrderTable getPersist() {
		OrderTable orderTable = new OrderTable();
		ExtractableResponse<Response> response = requestToCreateOrderTable(orderTable);
		return response.jsonPath().getObject(".", OrderTable.class);
	}

	public static ExtractableResponse<Response> requestToChangeEmpty(OrderTable orderTable) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when()
			.put("/api/tables/" + orderTable.getId() + "/empty")
			.then().log().all()
			.extract();
	}

	public static void assertThatChangeEmpty(ExtractableResponse<Response> response, boolean isEmpty) {
		OrderTable actual = response.jsonPath().getObject(".", OrderTable.class);

		assertThat(actual.isEmpty()).isEqualTo(isEmpty);
	}

	public static ExtractableResponse<Response> requestToChangeNumberOfGuests(OrderTable orderTable) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTable)
			.when()
			.put("/api/tables/" + orderTable.getId() + "/number-of-guests")
			.then().log().all()
			.extract();
	}

	public static void assertThatChangeNumberOfGuests(ExtractableResponse<Response> response, int numberOfGuests) {
		OrderTable actual = response.jsonPath().getObject(".", OrderTable.class);

		assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	public static OrderTable getEmptyPersist() {
		OrderTable orderTable = new OrderTable();
		ExtractableResponse<Response> response = requestToCreateOrderTable(orderTable);

		OrderTable persist = response.jsonPath().getObject(".", OrderTable.class);
		persist.setEmpty(true);
		ExtractableResponse<Response> emptyResponse = requestToChangeEmpty(persist);

		return emptyResponse.jsonPath().getObject(".", OrderTable.class);
	}
}
