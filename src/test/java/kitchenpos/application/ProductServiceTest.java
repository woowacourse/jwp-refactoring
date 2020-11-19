package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Money;
import kitchenpos.domain.Product;

class ProductServiceTest extends ServiceTest {
	@Autowired
	private ProductService productService;

	@DisplayName("price가 null일 경우 - IllegalArgumentException 발생")
	@Test
	void create_whenPriceIsNull_thenThrowIllegalArgumentException() {
		Product product = createProduct(null, "김", null);

		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("price가 음수일 경우 - IllegalArgumentException 발생")
	@Test
	void create_whenPriceIsMinus_thenThrowIllegalArgumentException() {
		Product product = createProduct(null, "김", new Money(-1L));

		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("product 저장 성공")
	@Test
	void create() {
		Product product = createProduct(null, "김", new Money(1000L));

		Product actual = productService.create(product);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(product.getName()),
			() -> assertThat(actual.getPrice()).isEqualTo(product.getPrice())
		);
	}

	@Test
	void list() {
		Product product = createProduct(null, "김", new Money(1000L));

		Product expect = productService.create(product);

		List<Product> actual = productService.list();
		Product actualItem = actual.get(0);
		assertThat(actual).hasSize(1);
		assertAll(
			() -> assertThat(actualItem.getName()).isEqualTo(expect.getName()),
			() -> assertThat(actualItem.getPrice()).isEqualTo(expect.getPrice())
		);
	}
}