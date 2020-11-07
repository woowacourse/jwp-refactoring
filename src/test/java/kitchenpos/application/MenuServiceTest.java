package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

class MenuServiceTest implements ServiceTest {

	@Autowired
	private MenuService menuService;

	@DisplayName("메뉴를 등록한다")
	@Test
	void create() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(1L);

		Menu input = new Menu();
		input.setName("후라이드");
		input.setPrice(BigDecimal.valueOf(16_000));
		input.setMenuGroupId(1L);
		input.setMenuProducts(Arrays.asList(menuProduct));

		Menu output = menuService.create(input);
		assertAll(
			() -> assertThat(output.getId()).isNotNull(),
			() -> assertThat(output.getName()).isEqualTo("후라이드"),
			() -> assertThat(output.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16_000)),
			() -> assertThat(output.getMenuGroupId()).isEqualTo(1L),
			() -> assertThat(output.getMenuProducts()).usingElementComparatorOnFields("id").isNotNull()
		);
	}

	@DisplayName("메뉴의 금액이 0 미만일 경우 예외가 발생한다")
	@ValueSource(longs = {-1, -10_000})
	@ParameterizedTest
	void create_WhenPriceIsLowerThatZero_ThrowException(long invalidPrice) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(1L);

		Menu input = new Menu();
		input.setName("후라이드");
		input.setPrice(BigDecimal.valueOf(invalidPrice));
		input.setMenuGroupId(1L);
		input.setMenuProducts(Arrays.asList(menuProduct));

		assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("메뉴가격이 메뉴를 구성하는 상품의 가격의 합과 같거나 저렴할 경우 정상동작한다")
	@ValueSource(longs = {32_000, 31_000})
	@ParameterizedTest
	void create_WhenTwoProductsPriceIsSameOrHigherThanMenuPrice_WorksWell(long validPrice) {
		MenuProduct price16_000MenuProduct1 = new MenuProduct();
		price16_000MenuProduct1.setProductId(1L);
		price16_000MenuProduct1.setQuantity(1L);
		MenuProduct price16_000MenuProduct2 = new MenuProduct();
		price16_000MenuProduct2.setProductId(2L);
		price16_000MenuProduct2.setQuantity(1L);

		Menu input = new Menu();
		input.setName("후라이드와 양념치킨");
		input.setPrice(BigDecimal.valueOf(validPrice));
		input.setMenuGroupId(1L);
		input.setMenuProducts(Arrays.asList(price16_000MenuProduct1, price16_000MenuProduct2));

		assertDoesNotThrow(() -> menuService.create(input));
	}

	@DisplayName("메뉴가격이 메뉴를 구성하는 상품의 가격의 합보다 비쌀 경우 예외가 발생한다")
	@ValueSource(longs = {33_000, 34_000})
	@ParameterizedTest
	void create_WhenTwoProductsPriceIsLowerThanMenuPrice_ThrowException(long validPrice) {
		MenuProduct price16_000MenuProduct1 = new MenuProduct();
		price16_000MenuProduct1.setProductId(1L);
		price16_000MenuProduct1.setQuantity(1L);
		MenuProduct price16_000MenuProduct2 = new MenuProduct();
		price16_000MenuProduct2.setProductId(2L);
		price16_000MenuProduct2.setQuantity(1L);

		Menu input = new Menu();
		input.setName("후라이드와 양념치킨");
		input.setPrice(BigDecimal.valueOf(validPrice));
		input.setMenuGroupId(1L);
		input.setMenuProducts(Arrays.asList(price16_000MenuProduct1, price16_000MenuProduct2));

		assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴가 메뉴그룹에 속해있지 않을 경우 예외가 발생한다")
	@Test
	void create_WhenMenuIsNotBelongToMenuGroup_ThrowException() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(1L);

		long invalidMenuGroupId = 1000L;

		Menu input = new Menu();
		input.setName("후라이드");
		input.setPrice(BigDecimal.valueOf(16_000));
		input.setMenuGroupId(invalidMenuGroupId);
		input.setMenuProducts(Arrays.asList(menuProduct));

		assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴를 조회한다")
	@Test
	void list() {
		assertThat(menuService.list()).isNotEmpty();
	}
}