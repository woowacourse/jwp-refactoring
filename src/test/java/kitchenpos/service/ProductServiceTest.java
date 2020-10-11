package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Product;

@DisplayName("ProductService 단위 테스트")
@SpringBootTest
class ProductServiceTest {
	@Autowired
	ProductService productService;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create() {
		Product input = new Product();
		input.setName("강정치킨");
		input.setPrice(BigDecimal.valueOf(17000));

		Product created = productService.create(input);

		assertAll(
			() -> assertThat(created.getId()).isEqualTo(7L),
			() -> assertThat(created.getName()).isEqualTo("강정치킨"),
			() -> assertThat(created.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000))
		);
	}

	@Test
	void list() {
	}
}