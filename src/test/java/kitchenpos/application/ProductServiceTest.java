package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductCreateResponse;
import kitchenpos.dto.product.ProductFindAllResponse;
import kitchenpos.dto.product.ProductFindAllResponses;

class ProductServiceTest extends ServiceTest {
	@Autowired
	private ProductService productService;

	@DisplayName("price가 null일 경우 - IllegalArgumentException 발생")
	@Test
	void create_whenPriceIsNull_thenThrowIllegalArgumentException() {
		ProductCreateRequest productCreateRequest = new ProductCreateRequest(null, "김", null);

		assertThatThrownBy(() -> productService.create(productCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("price가 음수일 경우 - IllegalArgumentException 발생")
	@Test
	void create_whenPriceIsMinus_thenThrowIllegalArgumentException() {
		ProductCreateRequest productCreateRequest = new ProductCreateRequest(null, "김", -1L);

		assertThatThrownBy(() -> productService.create(productCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("product 저장 성공")
	@Test
	void create() {
		ProductCreateRequest productCreateRequest = new ProductCreateRequest(null, "김", 1000L);

		ProductCreateResponse actual = productService.create(productCreateRequest);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(productCreateRequest.getName()),
			() -> assertThat(actual.getPrice()).isEqualTo(productCreateRequest.getPrice())
		);
	}

	@Test
	void list() {
		ProductCreateRequest productCreateRequest = new ProductCreateRequest(null, "김", 1000L);

		ProductCreateResponse expect = productService.create(productCreateRequest);

		ProductFindAllResponses actual = productService.findAll();
		ProductFindAllResponse actualItem = actual.getProductFindAllResponses().get(0);
		assertThat(actual.getProductFindAllResponses()).hasSize(1);
		assertAll(
			() -> assertThat(actualItem.getName()).isEqualTo(expect.getName()),
			() -> assertThat(actualItem.getPrice()).isEqualTo(expect.getPrice())
		);
	}
}