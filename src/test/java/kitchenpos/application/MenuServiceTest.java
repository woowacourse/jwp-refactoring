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

	private Menu menu1;

	private Menu menu2;

	private MenuProduct menuProduct1;

	private MenuProduct menuProduct2;

	private Product product1;

	@BeforeEach
	void setUp() {
		this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		product1 = new Product();
		product1.setId(1L);
		product1.setPrice(BigDecimal.valueOf(16000));

		menuProduct1 = new MenuProduct();
		menuProduct1.setMenuId(1L);
		menuProduct1.setProductId(1L);
		menuProduct1.setQuantity(3);
		menuProduct2 = new MenuProduct();
		menuProduct2.setMenuId(2L);
		menuProduct2.setProductId(2L);
		menuProduct2.setQuantity(5);

		menu1 = new Menu();
		menu1.setId(1L);
		menu1.setName("Fried");
		menu1.setPrice(BigDecimal.valueOf(16000));
		menu1.setMenuProducts(Collections.singletonList(menuProduct1));
		menu2 = new Menu();
		menu2.setId(2L);
		menu2.setName("Garlic");
		menu2.setPrice(BigDecimal.valueOf(17000));
		menu2.setMenuProducts(Collections.singletonList(menuProduct2));
	}

	@DisplayName("Menu를 생성한다.")
	@Test
	void createTest() {
		when(menuGroupDao.existsById(any())).thenReturn(true);
		when(productDao.findById(any())).thenReturn(Optional.of(product1));
		when(menuDao.save(any())).thenReturn(menu1);
		when(menuProductDao.save(any())).thenReturn(menuProduct1);

		Menu savedMenu = menuService.create(menu1);

		assertAll(
			() -> assertThat(savedMenu.getId()).isEqualTo(menu1.getId()),
			() -> assertThat(savedMenu.getName()).isEqualTo(menu1.getName()),
			() -> assertThat(savedMenu.getPrice()).isEqualTo(menu1.getPrice()),
			() -> assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isEqualTo(menu1.getId()),
			() -> assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(3)
		);
	}

	@DisplayName("Menu의 price가 null이면 예외 발생한다.")
	@Test
	void priceNullException() {
		menu1.setPrice(null);
		assertThatThrownBy(() -> menuService.create(menu1))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Menu의 price가 0보다 작으면 예외 발생한다.")
	@Test
	void negativePriceException() {
		menu1.setPrice(BigDecimal.valueOf(-1));
		assertThatThrownBy(() -> menuService.create(menu1))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("Menu의 price가 메뉴에 포함된 MenuProduct의 price 합보다 크면 예외 발생한다.")
	@Test
	void priceaNullException() {
		BigDecimal sum = product1.getPrice().multiply(BigDecimal.valueOf(menuProduct1.getQuantity()));
		menu1.setPrice(sum.add(BigDecimal.ONE));

		assertThatThrownBy(() -> menuService.create(menu1))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록된 모든 Menu를 조회한다.")
	@Test
	void listTest() {
		List<Menu> menus = Arrays.asList(menu1, menu2);
		List<MenuProduct> menuProducts1 = Collections.singletonList(menuProduct1);
		List<MenuProduct> menuProducts2 = Collections.singletonList(menuProduct2);
		when(menuDao.findAll()).thenReturn(menus);
		when(menuProductDao.findAllByMenuId(1L)).thenReturn(menuProducts1);
		when(menuProductDao.findAllByMenuId(2L)).thenReturn(menuProducts2);

		List<Menu> searched = menuService.list();

		assertAll(
			() -> assertThat(searched.get(0).getId()).isEqualTo(menu1.getId()),
			() -> assertThat(searched.get(0).getName()).isEqualTo(menu1.getName()),
			() -> assertThat(searched.get(0).getMenuProducts().get(0).getMenuId()).isEqualTo(menu1.getId()),
			() -> assertThat(searched.get(1).getId()).isEqualTo(menu2.getId()),
			() -> assertThat(searched.get(1).getName()).isEqualTo(menu2.getName()),
			() -> assertThat(searched.get(1).getMenuProducts().get(0).getMenuId()).isEqualTo(menu2.getId())
		);
	}
}
