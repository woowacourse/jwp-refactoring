package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
	private MenuService menuService;

	@Mock
	private JdbcTemplateMenuDao menuDao;

	@Mock
	private JdbcTemplateMenuGroupDao menuGroupDao;

	@Mock
	private JdbcTemplateMenuProductDao menuProductDao;

	@Mock
	private JdbcTemplateProductDao productDao;

	@BeforeEach
	void setUp() {
		this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
	}

	@DisplayName("Menu를 생성한다.")
	@Test
	void createTest() {
		Product friedProduct = new Product();
		friedProduct.setId(1L);
		friedProduct.setPrice(BigDecimal.valueOf(16000));

		MenuProduct friedMenuProduct = new MenuProduct();
		friedMenuProduct.setMenuId(1L);
		friedMenuProduct.setProductId(1L);
		friedMenuProduct.setQuantity(3);

		Menu friedChicken = new Menu();
		friedChicken.setId(1L);
		friedChicken.setName("Fried");
		friedChicken.setPrice(BigDecimal.valueOf(16000));
		friedChicken.setMenuProducts(Collections.singletonList(friedMenuProduct));

		when(menuGroupDao.existsById(any())).thenReturn(true);
		when(productDao.findById(any())).thenReturn(Optional.of(friedProduct));
		when(menuDao.save(any())).thenReturn(friedChicken);
		when(menuProductDao.save(any())).thenReturn(friedMenuProduct);

		Menu savedMenu = menuService.create(friedChicken);

		assertAll(
			() -> assertThat(savedMenu.getId()).isEqualTo(friedChicken.getId()),
			() -> assertThat(savedMenu.getName()).isEqualTo(friedChicken.getName()),
			() -> assertThat(savedMenu.getPrice()).isEqualTo(friedChicken.getPrice()),
			() -> assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isEqualTo(friedChicken.getId()),
			() -> assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(3)
		);
	}

	@DisplayName("Menu의 price가 null이면 예외 발생한다.")
	@Test
	void priceNullException() {
		Menu friedChicken = new Menu();
		friedChicken.setPrice(null);

		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Menu의 price가 0보다 작으면 예외 발생한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -100})
	void negativePriceException(int price) {
		Menu friedChicken = new Menu();
		friedChicken.setPrice(BigDecimal.valueOf(price));

		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Menu의 price가 메뉴에 포함된 MenuProduct의 price 합보다 크면 예외 발생한다.")
	@Test
	void differentSumWithMenuPriceSum() {
		Product friedProduct = new Product();
		friedProduct.setPrice(BigDecimal.valueOf(16000));
		MenuProduct friedMenuProduct = new MenuProduct();
		friedMenuProduct.setQuantity(3);
		Menu friedChicken = new Menu();

		BigDecimal sum = friedProduct.getPrice().multiply(BigDecimal.valueOf(friedMenuProduct.getQuantity()));
		friedChicken.setPrice(sum.add(BigDecimal.ONE));

		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("존재하지 않는 메뉴 그룹인 경우 예외 발생한다.")
	@Test
	void notExistMenuGroupException() {
		Menu friedChicken = new Menu();
		friedChicken.setPrice(BigDecimal.valueOf(16000));
		when(menuGroupDao.existsById(any())).thenReturn(false);

		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록된 모든 Menu를 조회한다.")
	@Test
	void listTest() {
		MenuProduct friedMenuProduct = new MenuProduct();
		friedMenuProduct.setMenuId(1L);
		MenuProduct garlicMenuProduct = new MenuProduct();
		garlicMenuProduct.setMenuId(2L);
		Menu friedChicken = new Menu();
		friedChicken.setId(1L);
		friedChicken.setName("Fried");
		friedChicken.setMenuProducts(Collections.singletonList(friedMenuProduct));
		Menu garlicChicken = new Menu();
		garlicChicken.setId(2L);
		garlicChicken.setName("Garlic");
		garlicChicken.setMenuProducts(Collections.singletonList(garlicMenuProduct));

		List<Menu> menus = Arrays.asList(friedChicken, garlicChicken);
		List<MenuProduct> menuProducts1 = Collections.singletonList(friedMenuProduct);
		List<MenuProduct> menuProducts2 = Collections.singletonList(garlicMenuProduct);
		when(menuDao.findAll()).thenReturn(menus);
		when(menuProductDao.findAllByMenuId(1L)).thenReturn(menuProducts1);
		when(menuProductDao.findAllByMenuId(2L)).thenReturn(menuProducts2);

		List<Menu> searched = menuService.list();

		assertAll(
			() -> assertThat(searched.get(0).getId()).isEqualTo(friedChicken.getId()),
			() -> assertThat(searched.get(0).getName()).isEqualTo(friedChicken.getName()),
			() -> assertThat(searched.get(0).getMenuProducts().get(0).getMenuId()).isEqualTo(friedChicken.getId()),
			() -> assertThat(searched.get(1).getId()).isEqualTo(garlicChicken.getId()),
			() -> assertThat(searched.get(1).getName()).isEqualTo(garlicChicken.getName()),
			() -> assertThat(searched.get(1).getMenuProducts().get(0).getMenuId()).isEqualTo(garlicChicken.getId())
		);
	}
}
