package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private MenuProductDao menuProductDao;

	@Mock
	private ProductDao productDao;

	private MenuService menuService;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
	}

	@DisplayName("menu price 가 null 일 때 IllegalArgumentException 발생")
	@Test
	void create1() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("메뉴");

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("menu price 가 음수일 때 IllegalArgumentException 발생")
	@Test
	void create2() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("메뉴");
		menu.setPrice(BigDecimal.valueOf(-1L));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴와 연관관계가 있는 메뉴 그룹의 아이디가 존재하지 않을 때 IllegalArgumentException 발생")
	@Test
	void create3() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("메뉴");
		menu.setPrice(BigDecimal.valueOf(1000L));
		menu.setMenuGroupId(1L);

		when(menuGroupDao.existsById(anyLong())).thenReturn(false);

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("productDao에 등록되지 않은 product를 menu가 소유할 때 IllegalArgumentException 발생")
	@Test
	void create5() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("메뉴");
		menu.setPrice(BigDecimal.valueOf(1000L));
		menu.setMenuGroupId(1L);

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setMenuId(1L);
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(1L);
		menuProduct.setSeq(1L);
		menu.setMenuProducts(Collections.singletonList(menuProduct));

		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴에 속한 상품 금액의 합(=sum)이 메뉴의 가격(=price) 보다 작을 때 IllegalArgumentException 발생")
	@Test
	void create6() {
		long menuPrice = 1000L;
		long productPrice = 500L;

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("메뉴");
		menu.setPrice(BigDecimal.valueOf(menuPrice));
		menu.setMenuGroupId(1L);

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setMenuId(1L);
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(1L);
		menuProduct.setSeq(1L);
		menu.setMenuProducts(Collections.singletonList(menuProduct));

		Product product = new Product();
		product.setId(1L);
		product.setName("김");
		product.setPrice(BigDecimal.valueOf(productPrice));

		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
		when(menuDao.save(any(Menu.class))).thenReturn(menu);

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 저장 성공")
	@Test
	void create7() {
		long menuPrice = 500L;
		long productPrice = 1000L;

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("메뉴");
		menu.setPrice(BigDecimal.valueOf(menuPrice));
		menu.setMenuGroupId(1L);

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setMenuId(1L);
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(1L);
		menuProduct.setSeq(1L);
		menu.setMenuProducts(Collections.singletonList(menuProduct));

		Product product = new Product();
		product.setId(1L);
		product.setName("김");
		product.setPrice(BigDecimal.valueOf(productPrice));

		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
		when(menuDao.save(any(Menu.class))).thenReturn(menu);

		Menu actual = menuService.create(menu);

		assertAll(
			() -> assertEquals(actual.getId(), menu.getId()),
			() -> assertEquals(actual.getMenuGroupId(), menu.getMenuGroupId()),
			() -> assertEquals(actual.getName(), menu.getName()),
			() -> assertEquals(actual.getPrice(), menu.getPrice()),
			() -> assertEquals(actual.getMenuProducts().size(), menu.getMenuProducts().size())
		);
	}

	@Test
	void list() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setPrice(BigDecimal.valueOf(1000L));
		menu.setName("김말이");
		menu.setMenuGroupId(1L);

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setMenuId(1L);
		menuProduct.setProductId(1L);
		menuProduct.setSeq(1L);
		menuProduct.setQuantity(1L);

		when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));
		when(menuProductDao.findAllByMenuId(anyLong())).thenReturn(Collections.singletonList(menuProduct));

		List<Menu> expected = menuService.list();
		assertThat(expected).hasSize(1);
	}
}