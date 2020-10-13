package kitchenpos.service.step;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuServiceTestStep {
	public static Menu createValidMenu() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(2);

		List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

		Menu menu = new Menu();
		menu.setName("후라이드+후라이드");
		menu.setPrice(BigDecimal.valueOf(19000));
		menu.setMenuGroupId(1L);
		menu.setMenuProducts(menuProducts);

		return menu;
	}

	public static Menu createMenuThatPriceLessThanZero() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(2);

		List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

		Menu menu = new Menu();
		menu.setName("후라이드+후라이드");
		menu.setPrice(BigDecimal.valueOf(-1));
		menu.setMenuGroupId(1L);
		menu.setMenuProducts(menuProducts);

		return menu;
	}

	public static Menu createMenuThatPriceBiggerThanSum() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(2);

		List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

		Menu menu = new Menu();
		menu.setName("후라이드+후라이드");
		menu.setPrice(BigDecimal.valueOf(32001));
		menu.setMenuGroupId(1L);
		menu.setMenuProducts(menuProducts);

		return menu;
	}

	public static Menu createMenuThatNotBelongToMenuGroup() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(2);

		List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

		Menu menu = new Menu();
		menu.setName("후라이드+후라이드");
		menu.setPrice(BigDecimal.valueOf(19000));
		menu.setMenuProducts(menuProducts);

		return menu;
	}
}
