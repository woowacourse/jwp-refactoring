package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	private ProductService productService;

	@Mock
	private JdbcTemplateProductDao productDao;

	@BeforeEach
	void setUp() {
		this.productService = new ProductService(productDao);
	}

	@DisplayName("Product를 생성한다.")
	@Test
	void createTest() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Fired");
		product.setPrice(BigDecimal.valueOf(16000));

		when(productDao.save(any())).thenReturn(product);

		Product saved = productService.create(product);

		assertThat(saved.getId()).isEqualTo(product.getId());
		assertThat(saved.getName()).isEqualTo(product.getName());
		assertThat(saved.getPrice()).isEqualTo(product.getPrice());
	}

	@DisplayName("상품의 price가 null이면 예외 발생한다.")
	@Test
	void nullProductPriceException() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Fired");
		product.setPrice(null);

		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품의 price가 0보다 작으면 예외 발생한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -100})
	void negativeProductPriceException(int price) {
		Product product = new Product();
		product.setId(1L);
		product.setName("Fired");
		product.setPrice(BigDecimal.valueOf(price));

		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록된 모든 Product를 조회한다.")
	@Test
	void listTest() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Fired");
		product.setPrice(BigDecimal.valueOf(16000));
		when(productDao.findAll()).thenReturn(Collections.singletonList(product));

		List<Product> products = productService.list();

		assertThat(products.size()).isEqualTo(1);
		assertThat(products.get(0).getId()).isEqualTo(product.getId());
		assertThat(products.get(0).getName()).isEqualTo(product.getName());
		assertThat(products.get(0).getPrice()).isEqualTo(product.getPrice());
	}
}
