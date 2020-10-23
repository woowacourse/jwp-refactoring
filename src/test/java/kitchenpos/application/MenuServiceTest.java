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

	private Menu friedChicken;

	private Menu garlicChicken;

	private MenuProduct friedMenuProduct;

	private MenuProduct garlicMenuProduct;

	private Product friedProduct;

	@BeforeEach
	void setUp() {
		this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		friedProduct = new Product();
		friedProduct.setId(1L);
		friedProduct.setPrice(BigDecimal.valueOf(16000));

		friedMenuProduct = new MenuProduct();
		friedMenuProduct.setMenuId(1L);
		friedMenuProduct.setProductId(1L);
		friedMenuProduct.setQuantity(3);
		garlicMenuProduct = new MenuProduct();
		garlicMenuProduct.setMenuId(2L);
		garlicMenuProduct.setProductId(2L);
		garlicMenuProduct.setQuantity(5);

		friedChicken = new Menu();
		friedChicken.setId(1L);
		friedChicken.setName("Fried");
		friedChicken.setPrice(BigDecimal.valueOf(16000));
		friedChicken.setMenuProducts(Collections.singletonList(friedMenuProduct));
		garlicChicken = new Menu();
		garlicChicken.setId(2L);
		garlicChicken.setName("Garlic");
		garlicChicken.setPrice(BigDecimal.valueOf(17000));
		garlicChicken.setMenuProducts(Collections.singletonList(garlicMenuProduct));
	}

	@DisplayName("Menu를 생성한다.")
	@Test
	void createTest() {
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
		friedChicken.setPrice(null);
		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Menu의 price가 0보다 작으면 예외 발생한다.")
	@Test
	void negativePriceException() {
		friedChicken.setPrice(BigDecimal.valueOf(-1));
		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Menu의 price가 메뉴에 포함된 MenuProduct의 price 합보다 크면 예외 발생한다.")
	@Test
	void priceaNullException() {
		BigDecimal sum = friedProduct.getPrice().multiply(BigDecimal.valueOf(friedMenuProduct.getQuantity()));
		friedChicken.setPrice(sum.add(BigDecimal.ONE));

		assertThatThrownBy(() -> menuService.create(friedChicken))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록된 모든 Menu를 조회한다.")
	@Test
	void listTest() {
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
