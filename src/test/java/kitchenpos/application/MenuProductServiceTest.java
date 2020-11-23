package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;

class MenuProductServiceTest extends ServiceTest {
	@Autowired
	private MenuProductService menuProductService;
	@Autowired
	private ProductRepository productRepository;

	@DisplayName("메뉴에 속한 상품 금액의 합(=sum)이 메뉴의 가격(=price) 보다 작을 때 IllegalArgumentException 발생")
	@Test
	void validSumIsLowerThanPrice_whenSumIsLowerThanPrice_thenThrowIllegalArgumentException() {
		Money menuPrice = new Money(1000L);
		Money productPrice = new Money(500L);
		long quantity = 1L;

		Product product = createProduct(null, "제품", productPrice);

		Product savedProduct = productRepository.save(product);

		MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), quantity, null);

		MenuProducts menuProducts = new MenuProducts(Collections.singletonList(menuProduct));

		assertThatThrownBy(() -> menuProductService.validSumIsLowerThanPrice(menuPrice, menuProducts))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴에 속한 상품 금액의 합(=sum)이 메뉴의 가격(=price)과 같아 예외 발생하지 않음")
	@Test
	void validSumIsLowerThanPrice_whenSumIsEqualToPrice_thenExceptionIsNotOccur() {
		Money menuPrice = new Money(1000L);
		Money productPrice = new Money(500L);
		long quantity = 2L;

		Product product = createProduct(null, "제품", productPrice);

		Product savedProduct = productRepository.save(product);

		MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), quantity, null);

		MenuProducts menuProducts = new MenuProducts(Collections.singletonList(menuProduct));

		menuProductService.validSumIsLowerThanPrice(menuPrice, menuProducts);
	}

	@DisplayName("메뉴에 속한 상품 금액의 합(=sum)이 메뉴의 가격(=price)보다 커 예외 발생하지 않음")
	@Test
	void validSumIsLowerThanPrice_whenSumIsLargerThanPrice_thenExceptionIsNotOccur() {
		Money menuPrice = new Money(1000L);
		Money productPrice = new Money(500L);
		long quantity = 3L;

		Product product = createProduct(null, "제품", productPrice);

		Product savedProduct = productRepository.save(product);

		MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), quantity, null);

		MenuProducts menuProducts = new MenuProducts(Collections.singletonList(menuProduct));

		menuProductService.validSumIsLowerThanPrice(menuPrice, menuProducts);
	}
}