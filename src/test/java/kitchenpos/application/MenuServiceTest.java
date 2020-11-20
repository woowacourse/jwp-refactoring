package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuCreateResponse;
import kitchenpos.dto.menu.MenuFindAllResponse;
import kitchenpos.dto.menu.MenuFindAllResponses;
import kitchenpos.dto.menu.MenuProductCreateRequests;

class MenuServiceTest extends ServiceTest {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuGroupRepository menuGroupRepository;

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("menu price 가 null 일 때 IllegalArgumentException 발생")
	@Test
	void create_whenMenuPriceIsNull_thenThrowIllegalArgumentException() {
		MenuGroup menuGroup = createMenuGroup(1L, "그룹");

		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(null, "메뉴", null, menuGroup.getId(),
			MenuProductCreateRequests.from(
				new MenuProducts(Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L)))));

		assertThatThrownBy(() -> menuService.create(menuCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("menu price 가 음수일 때 IllegalArgumentException 발생")
	@Test
	void create_whenMenuPriceIsMinus_thenThrowIllegalArgumentException() {
		MenuGroup menuGroup = createMenuGroup(1L, "그룹");
		Menu menu = createMenu(null, "메뉴", new Money(-1L), menuGroup.getId(),
			new MenuProducts(Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L))));
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu);

		assertThatThrownBy(() -> menuService.create(menuCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴와 연관관계가 있는 메뉴 그룹의 아이디가 존재하지 않을 때 IllegalArgumentException 발생")
	@Test
	void create_whenNotExistMenuGroup_thenThrowIllegalArgumentException() {
		MenuGroup menuGroup = createMenuGroup(1L, "그룹");
		Menu menu = createMenu(null, "메뉴", new Money(100L), menuGroup.getId(),
			new MenuProducts(Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L))));
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu);

		assertThatThrownBy(() -> menuService.create(menuCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("productDao에 등록되지 않은 product를 menu가 소유할 때 IllegalArgumentException 발생")
	@Test
	void create_whenNotExistProduct_thenThrowIllegalArgumentException() {
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(null, "메뉴", new Money(100L), menuGroup.getId(),
			new MenuProducts(Collections.singletonList(createMenuProduct(null, 1L, 2L, 3L))));
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu);

		assertThatThrownBy(() -> menuService.create(menuCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴에 속한 상품 금액의 합(=sum)이 메뉴의 가격(=price) 보다 작을 때 IllegalArgumentException 발생")
	@Test
	void create_whenSumIsLowerThanPrice_thenThrowIllegalArgumentException() {
		long menuPrice = 1000L;
		long productPrice = 500L;
		long quantity = 1L;

		Product product = productRepository.save(createProduct(null, "제품", new Money(productPrice)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), quantity, 3L);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", new Money(menuPrice), menuGroup.getId(),
			new MenuProducts(Collections.singletonList(menuProduct)));
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu);

		assertThatThrownBy(() -> menuService.create(menuCreateRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 저장 성공")
	@Test
	void create() {
		long menuPrice = 1000L;
		long productPrice = 500L;
		long quantity = 2L;

		Product product = productRepository.save(createProduct(null, "제품", new Money(productPrice)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), quantity, null);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", new Money(menuPrice), menuGroup.getId(),
			new MenuProducts(Collections.singletonList(menuProduct)));
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu);

		MenuCreateResponse actual = menuService.create(menuCreateRequest);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(menu.getName()),
			() -> assertThat(actual.getPrice()).isEqualTo(menu.getPrice().getValue()),
			() -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
			() -> assertThat(actual.getMenuProducts().getMenuProductCreateResponses().get(0).getQuantity()).isEqualTo(
				2L)
		);
	}

	@Test
	void list() {
		long menuPrice = 1000L;
		long productPrice = 500L;

		Product product = productRepository.save(createProduct(null, "제품", new Money(productPrice)));
		MenuProduct menuProduct = createMenuProduct(null, product.getId(), 2L, null);
		MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
		Menu menu = createMenu(1L, "메뉴", new Money(menuPrice), menuGroup.getId(),
			new MenuProducts(Collections.singletonList(menuProduct)));
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu);

		MenuCreateResponse expect = menuService.create(menuCreateRequest);

		MenuFindAllResponses actual = menuService.list();
		MenuFindAllResponse actualItem = actual.getMenuFindAllResponses().get(0);

		assertThat(actual.getMenuFindAllResponses()).hasSize(1);
		assertAll(
			() -> assertThat(actualItem.getName()).isEqualTo(expect.getName()),
			() -> assertThat(actualItem.getPrice()).isEqualTo(expect.getPrice()),
			() -> assertThat(actualItem.getMenuGroupId()).isEqualTo(expect.getMenuGroupId()),
			() -> assertThat(
				actualItem.getMenuProducts().getMenuProductFindAllResponses().get(0).getQuantity()).isEqualTo(
				expect.getMenuProducts().getMenuProductCreateResponses().get(0).getQuantity())
		);
	}
}