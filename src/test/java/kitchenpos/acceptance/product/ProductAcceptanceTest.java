package kitchenpos.acceptance.product;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceStep;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Product;

public class ProductAcceptanceTest extends AcceptanceTest {

	@DisplayName("상품을 생성한다")
	@Test
	void create() {
		// given
		Product product = ProductAcceptanceStep.createProduct("방어회 (소)", 24_000);

		// when
		ExtractableResponse<Response> response = ProductAcceptanceStep.requestToCreateProduct(product);

		// then
		AcceptanceStep.assertThatStatusIsCreated(response);
		ProductAcceptanceStep.assertThatCreateProduct(response, product);
	}

	@DisplayName("모든 상품을 조회한다")
	@Test
	void list() {
		// given
		Product product = ProductAcceptanceStep.createProduct("방어회 (소)", 24_000);
		Product product2 = ProductAcceptanceStep.createProduct("방어회 (중)", 32_000);

		ProductAcceptanceStep.requestToCreateProduct(product);
		ProductAcceptanceStep.requestToCreateProduct(product2);

		// when
		ExtractableResponse<Response> response = ProductAcceptanceStep.requestToFindProducts();

		//then
		AcceptanceStep.assertThatStatusIsOk(response);
		ProductAcceptanceStep.assertThatFindProducts(response, Arrays.asList(product, product2));
	}
}
