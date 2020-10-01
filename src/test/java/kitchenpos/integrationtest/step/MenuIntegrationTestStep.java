package kitchenpos.integrationtest.step;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuIntegrationTestStep {

	public static Map<String, Object> createValidMenu() {
		Map<String, String> oneFriedChicken = new HashMap<>();
		oneFriedChicken.put("productId", "1");
		oneFriedChicken.put("quantity", "1");

		Map<String, String> twoSourceChicken = new HashMap<>();
		twoSourceChicken.put("productId", "2");
		twoSourceChicken.put("quantity", "2");

		List<Map<String, String>> menuProducts = Arrays.asList(
			oneFriedChicken,
			twoSourceChicken
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨 세트");
		requestBody.put("price", "35000");
		requestBody.put("menuGroupId", "1");
		requestBody.put("menuProducts", menuProducts);

		return requestBody;
	}

	public static Map<String, Object> createMenuThatPriceIsLessThanZero() {
		Map<String, String> oneFriedChicken = new HashMap<>();
		oneFriedChicken.put("productId", "1");
		oneFriedChicken.put("quantity", "1");

		Map<String, String> twoSourceChicken = new HashMap<>();
		twoSourceChicken.put("productId", "2");
		twoSourceChicken.put("quantity", "2");

		List<Map<String, String>> menuProducts = Arrays.asList(
			oneFriedChicken,
			twoSourceChicken
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨 세트");
		requestBody.put("price", "-1");
		requestBody.put("menuGroupId", "1");
		requestBody.put("menuProducts", menuProducts);

		return requestBody;
	}

	public static Map<String, Object> createMenuThatPriceIsBiggerThanSum() {
		Map<String, String> oneFriedChicken = new HashMap<>();
		oneFriedChicken.put("productId", "1");
		oneFriedChicken.put("quantity", "1");

		Map<String, String> twoSourceChicken = new HashMap<>();
		twoSourceChicken.put("productId", "2");
		twoSourceChicken.put("quantity", "2");

		List<Map<String, String>> menuProducts = Arrays.asList(
			oneFriedChicken,
			twoSourceChicken
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨 세트");
		requestBody.put("price", "49000");
		requestBody.put("menuGroupId", "1");
		requestBody.put("menuProducts", menuProducts);

		return requestBody;
	}
}
