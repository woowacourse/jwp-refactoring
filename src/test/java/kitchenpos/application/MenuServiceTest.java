package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceIntegrateTest {


  @Autowired
  private MenuService menuService;

  @Test
  @DisplayName("메뉴를 등록할 수 있다.")
  void create_success() {
    //given
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(1L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setMenuGroupId(1L);
    menu.setMenuProducts(List.of(menuProduct));

    //when
    final Menu actual = menuService.create(menu);

    //then
    assertThat(actual).isNotNull();

  }

  @Test
  @DisplayName("메뉴를 등록할 때 메뉴의 가격이 0보다 작으면 예외를 반환한다.")
  void create_fail_negative_price() {
    //given
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(1L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(-1));
    menu.setMenuGroupId(1L);
    menu.setMenuProducts(List.of(menuProduct));

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("메뉴를 등록할 때 메뉴그룹이 존재하지 않으면 예외를 반환한다.")
  void create_fail_not_exist_menuGroup() {
    //given
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(1L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setMenuGroupId(999L);
    menu.setMenuProducts(List.of(menuProduct));

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("메뉴를 등록할 때 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
  void create_fail_not_exist_product() {
    //given
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(999L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setMenuGroupId(1L);
    menu.setMenuProducts(List.of(menuProduct));

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("메뉴를 등록할 때 메뉴의 가격이 메뉴를 구성하는 상품들의 총 가격보다 크면 예외를 반환한다.")
  void create_fail_over_price() {
    //given
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(1L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(40000));
    menu.setMenuGroupId(1L);
    menu.setMenuProducts(List.of(menuProduct));

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 메뉴 목록을 조회할 수 있다.")
  void list_success() {
    // given, when
    final List<Menu> actual = menuService.list();

    //then
    assertThat(actual).hasSize(6);
  }
}