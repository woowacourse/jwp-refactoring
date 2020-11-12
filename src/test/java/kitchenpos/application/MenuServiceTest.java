package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends ServiceTest {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuGroupDao menuGroupDao;

	@Autowired
	private ProductDao productDao;

	@DisplayName("menu price 가 null 일 때 IllegalArgumentException 발생")
	@Test
	void create_whenMenuPriceIsNull_thenThrowIllegalArgumentException() {
		Menu menu = createMenu(null, "메뉴", null, 1L, Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L)));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("menu price 가 음수일 때 IllegalArgumentException 발생")
	@Test
	void create_whenMenuPriceIsMinus_thenThrowIllegalArgumentException() {
		Menu menu = createMenu(null, "메뉴", BigDecimal.valueOf(-1L), 1L,
			Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L)));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴와 연관관계가 있는 메뉴 그룹의 아이디가 존재하지 않을 때 IllegalArgumentException 발생")
	@Test
	void create_whenNotExistMenuGroup_thenThrowIllegalArgumentException() {
		Menu menu = createMenu(null, "메뉴", BigDecimal.valueOf(100L), 1L,
			Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L)));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("productDao에 등록되지 않은 product를 menu가 소유할 때 IllegalArgumentException 발생")
	@Test
	void create_whenNotExistProduct_thenThrowIllegalArgumentException() {
		MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(null, "메뉴", BigDecimal.valueOf(100L), menuGroup.getId(),
			Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L)));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴에 속한 상품 금액의 합(=sum)이 메뉴의 가격(=price) 보다 작을 때 IllegalArgumentException 발생")
	@Test
	void create_whenSumIsLowerThanPrice_thenThrowIllegalArgumentException() {
		long menuPrice = 1000L;
		long productPrice = 500L;
		long quantity = 1L;

		Product product = productDao.save(createProduct(null, "제품", BigDecimal.valueOf(productPrice)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), quantity, 3L);
		MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", BigDecimal.valueOf(menuPrice), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 저장 성공")
	@Test
	void create() {
		long menuPrice = 1000L;
		long productPrice = 500L;
		long quantity = 2L;

		Product product = productDao.save(createProduct(null, "제품", BigDecimal.valueOf(productPrice)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), quantity, 7L);
		MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", BigDecimal.valueOf(menuPrice), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu actual = menuService.create(menu);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(menu.getName()),
			() -> assertThat(actual.getPrice().longValue()).isEqualTo(menu.getPrice().longValue()),
			() -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
			() -> assertThat(actual.getMenuProducts().get(0).getQuantity()).isEqualTo(2L)
		);
	}

	@Test
	void list() {
		long menuPrice = 1000L;
		long productPrice = 500L;

		Product product = productDao.save(createProduct(null, "제품", BigDecimal.valueOf(productPrice)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), 2L, 7L);
		MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", BigDecimal.valueOf(menuPrice), menuGroup.getId(),
			Collections.singletonList(menuProduct));

		Menu expect = menuService.create(menu);

		List<Menu> actual = menuService.list();

		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).usingRecursiveComparison()
			.isEqualTo(expect);
	}
}