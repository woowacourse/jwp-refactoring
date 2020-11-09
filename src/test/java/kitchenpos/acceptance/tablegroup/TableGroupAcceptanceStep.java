package kitchenpos.acceptance.tablegroup;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.ordertable.OrderTableAcceptanceStep;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceStep {

	public static ExtractableResponse<Response> requestToCreateTableGroup(TableGroup tableGroup) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tableGroup)
			.when()
			.post("/api/table-groups")
			.then().log().all()
			.extract();
	}

	public static void assertThatCreateTableGroup(ExtractableResponse<Response> response, TableGroup expected) {
		TableGroup actual = response.jsonPath().getObject(".", TableGroup.class);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getCreatedDate()).isNotNull()
		);
	}

	public static TableGroup getPersist() {
		OrderTable orderTable = OrderTableAcceptanceStep.getEmptyPersist();
		OrderTable orderTable2 = OrderTableAcceptanceStep.getEmptyPersist();

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable, orderTable2));

		ExtractableResponse<Response> response = requestToCreateTableGroup(tableGroup);
		return response.jsonPath().getObject(".", TableGroup.class);
	}

	public static ExtractableResponse<Response> requestToUngroupTableGroup(TableGroup tableGroup) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.delete("/api/table-groups/" + tableGroup.getId())
			.then().log().all()
			.extract();
	}
}
