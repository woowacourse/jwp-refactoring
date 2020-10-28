package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductDao productDao;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productDao);
	}

	@DisplayName("price가 null일 경우 - IllegalArgumentException 발생")
	@Test
	void create1() {
		Product product = new Product();
		product.setId(1L);
		product.setName("김");
		product.setPrice(null);

		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("price가 음수일 경우 - IllegalArgumentException 발생")
	@Test
	void create2() {
		Product product = new Product();
		product.setId(1L);
		product.setName("김");
		product.setPrice(BigDecimal.valueOf(-1));

		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("product 저장 성공")
	@Test
	void create3() {
		Product product = new Product();
		product.setId(1L);
		product.setName("김");
		product.setPrice(BigDecimal.valueOf(1000));

		when(productDao.save(any(Product.class))).thenReturn(product);

		Product actual = productService.create(product);
		assertThat(product).usingRecursiveComparison().isEqualTo(actual);
	}

	@Test
	void list() {
		Product product = new Product();
		product.setId(1L);
		product.setName("김");
		product.setPrice(BigDecimal.valueOf(1000));

		when(productDao.findAll()).thenReturn(Collections.singletonList(product));

		List<Product> actual = productService.list();
		Product actualItem = actual.get(0);
		assertThat(product).usingRecursiveComparison().isEqualTo(actualItem);
	}
}