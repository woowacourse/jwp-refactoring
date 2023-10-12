package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

  @Autowired
  private MenuService menuService;

  @Autowired
  private MenuProductDao menuProductDao;

  @Test
  @DisplayName("create() : 메뉴를 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Menu menu = new Menu();
    menu.setMenuGroupId(3L);
    menu.setName("menu1MenuGroup3");
    menu.setPrice(BigDecimal.valueOf(13000));
    menu.setMenuProducts(menuProductDao.findAll());

    //when
    final Menu savedMenu = menuService.create(menu);

    //then
    assertAll(
        () -> assertNotNull(savedMenu.getId()),
        () -> assertEquals(menu.getName(), savedMenu.getName()),
        () -> assertEquals(menu.getMenuGroupId(), savedMenu.getMenuGroupId()),
        () -> assertEquals(0, menu.getPrice().compareTo(savedMenu.getPrice()))
    );
  }

  @Test
  @DisplayName("create() : 메뉴 가격은 메뉴 상품들의 합보다 크거나 같으면 IllegalArgumentException이 발생할 수 있다.")
  void test_create_IllegalArgumentException() throws Exception {
    //given
    final Menu menu = new Menu();
    menu.setMenuGroupId(3L);
    menu.setName("menu1MenuGroup3");
    menu.setPrice(BigDecimal.valueOf(100000000));
    menu.setMenuProducts(menuProductDao.findAll());

    //when & then
    Assertions.assertThatThrownBy(() -> menuService.create(menu))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("list() : 모든 메뉴들을 조회할 수 있다.")
  void test_list() throws Exception {
    //when
    final List<Menu> menus = menuService.list();

    //then
    assertEquals(6, menus.size());
  }
}
