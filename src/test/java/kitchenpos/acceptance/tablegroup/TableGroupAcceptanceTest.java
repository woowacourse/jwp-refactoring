package kitchenpos.acceptance.tablegroup;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceStep;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.ordertable.OrderTableAcceptanceStep;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTest extends AcceptanceTest {

	@DisplayName("빈 테이블들을 단체로 지정한다")
	@Test
	void create() {
		// given
		OrderTable orderTable = OrderTableAcceptanceStep.getEmptyPersist();
		OrderTable orderTable2 = OrderTableAcceptanceStep.getEmptyPersist();

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable, orderTable2));

		// when
		ExtractableResponse<Response> response = TableGroupAcceptanceStep.requestToCreateTableGroup(tableGroup);

		// then
		AcceptanceStep.assertThatStatusIsCreated(response);
		TableGroupAcceptanceStep.assertThatCreateTableGroup(response);
	}

	@DisplayName("테이블 단체 지정을 해제한다")
	@Test
	void upgroup() {
		// given
		TableGroup tableGroup = TableGroupAcceptanceStep.getPersist();

		// when
		ExtractableResponse<Response> response = TableGroupAcceptanceStep.requestToUngroupTableGroup(tableGroup);

		// then
		AcceptanceStep.assertThatStatusIsNoContent(response);
	}
}
