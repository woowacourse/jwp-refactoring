package kitchenpos.acceptance.product;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;

public class ProductAcceptanceStep {

	public static Product create(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(BigDecimal.valueOf(price));

		return product;
	}

	public static ExtractableResponse<Response> requestToCreateProduct(Product product) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(product)
			.when()
			.post("/api/products")
			.then().log().all()
			.extract();
	}

	public static void assertThatCreateProduct(ExtractableResponse<Response> response, Product expected) {
		Product actual = response.jsonPath().getObject(".", Product.class);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice())
		);
	}

	public static ExtractableResponse<Response> requestToFindProducts() {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/products")
			.then().log().all()
			.extract();
	}

	public static void assertThatFindProducts(ExtractableResponse<Response> response, List<Product> expected) {
		List<Product> actual = response.jsonPath().getList(".", Product.class);

		assertAll(
			() -> assertThat(actual).usingElementComparatorOnFields("id").isNotNull(),
			() -> assertThat(actual).usingElementComparatorOnFields("name").isEqualTo(expected)
		);
	}

	public static Product getPersist(String name, int price) {
		Product product = create(name, price);
		ExtractableResponse<Response> response = requestToCreateProduct(product);
		return response.jsonPath().getObject(".", Product.class);
	}
}
