package kitchenpos.integrationtest.step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderIntegrationTestStep {
	public static Map<String, Object> createValidOrder() {
		Map<String, Object> orderLineItem = new HashMap<>();
		orderLineItem.put("menuId", 1);
		orderLineItem.put("quantity", 1);

		List<Map<String, Object>> orderLineItems = new ArrayList<>();
		orderLineItems.add(orderLineItem);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderTableId", 2);
		requestBody.put("orderLineItems", orderLineItems);

		return requestBody;
	}

	public static Map<String, Object> createOrderToEmptyTable() {
		Map<String, Object> orderLineItem = new HashMap<>();
		orderLineItem.put("menuId", 1);
		orderLineItem.put("quantity", 1);

		List<Map<String, Object>> orderLineItems = new ArrayList<>();
		orderLineItems.add(orderLineItem);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderTableId", 1);
		requestBody.put("orderLineItems", orderLineItems);

		return requestBody;
	}
}
