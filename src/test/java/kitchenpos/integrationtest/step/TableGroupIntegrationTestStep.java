package kitchenpos.integrationtest.step;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TableGroupIntegrationTestStep {

	public static Map<String, Object> createValidTableGroup() {
		Map<String, Object> orderTables1 = new HashMap<>();
		orderTables1.put("id", 1);

		Map<String, Object> orderTables2 = new HashMap<>();
		orderTables2.put("id", 3);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderTables", Arrays.asList(
			orderTables1,
			orderTables2
		));

		return requestBody;
	}

	public static Map<String, Object> createTableGroupThatHasDuplicatedTables() {
		Map<String, Object> orderTables1 = new HashMap<>();
		orderTables1.put("id", 1);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderTables", Arrays.asList(
			orderTables1,
			orderTables1
		));

		return requestBody;
	}
}
