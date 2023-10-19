package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupRepositoryImpl;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.MenuRepositoryImpl;
import kitchenpos.dao.ProductRepositoryImpl;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.ServiceIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceIntegrationTest {

  @Autowired
  private MenuService menuService;

  @Autowired
  private ProductRepositoryImpl productRepository;

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  @Autowired
  private MenuRepositoryImpl menuRepository;

  private Product product;
  private MenuGroup menuGroup;

  @BeforeEach
  void setUp() {
    product = productRepository.save(ProductFixture.createProduct());
    menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
  }

  @Test
  @DisplayName("create() : 메뉴를 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Menu menu = MenuFixture.createMenu(menuGroup, product);

    //when
    final Menu savedMenu = menuService.create(menu);

    //then
    assertAll(
        () -> assertNotNull(savedMenu.getId()),
        () -> assertEquals(menu.getName(), savedMenu.getName()),
        () -> assertEquals(menu.getMenuGroup().getId(), savedMenu.getMenuGroup().getId()),
        () -> assertEquals(0, menu.getPrice().compareTo(savedMenu.getPrice()))
    );
  }

  @Test
  @DisplayName("create() : 메뉴 가격은 메뉴 상품들의 합보다 크거나 같으면 IllegalArgumentException이 발생할 수 있다.")
  void test_create_IllegalArgumentException() throws Exception {
    //given
    //메뉴 상품 가격 합 : 10
    //메뉴 가격 : 100000000
    final Menu menu = MenuFixture.createExceedPriceMenu(menuGroup, product);

    //when & then
    Assertions.assertThatThrownBy(() -> menuService.create(menu))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("list() : 모든 메뉴들을 조회할 수 있다.")
  void test_list() throws Exception {
    //given
    menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menuRepository.save(MenuFixture.createMenu(menuGroup, product));

    //when
    final List<Menu> menus = menuService.list();

    //then
    assertEquals(4, menus.size());
  }
}
