package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Product;

class ProductServiceTest implements ServiceTest {

	@Autowired
	private ProductService productService;

	@DisplayName("상품을 등록한다")
	@Test
	void create() {
		Product input = new Product();
		input.setName("전어회(세꼬시)");
		input.setPrice(BigDecimal.valueOf(15_000));
		Product output = productService.create(input);

		assertAll(
			() -> assertThat(output.getId()).isNotNull(),
			() -> assertThat(output.getName()).isEqualTo("전어회(세꼬시)"),
			() -> assertThat(output.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(15_000))
		);
	}

	@DisplayName("가격이 0원 미만일 경우 예외가 발생한다")
	@ValueSource(ints = {-10_000_000, -1})
	@ParameterizedTest
	void create_ThrowException_WhenInvalidPrice(int invalidPrice) {
		Product input = new Product();
		input.setName("전어회(세꼬시)");
		input.setPrice(BigDecimal.valueOf(invalidPrice));

		assertThatIllegalArgumentException().isThrownBy(() -> productService.create(input));
	}

	@DisplayName("모든 상품을 조회한다")
	@Test
	void read_AllProducts() {
		List<Product> outputs = productService.list();
		assertThat(outputs.size()).isNotZero();
	}
}