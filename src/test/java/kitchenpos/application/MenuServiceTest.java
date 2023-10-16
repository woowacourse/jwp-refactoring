package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.만냥치킨_2마리;
import static kitchenpos.fixture.MenuFixture.만냥치킨_2마리_잘못된_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.persistence.MenuDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceIntegrateTest {

  @Autowired
  private MenuService menuService;
  @Autowired
  private MenuDao menuDao;

  @Test
  @DisplayName("메뉴를 등록할 수 있다.")
  void create_success() {
    //given, when
    final Menu savedMenu = menuService.create(만냥치킨_2마리());
    final Menu actual = menuDao.findById(savedMenu.getId()).get();

    //then
    Assertions.assertAll(
        () -> assertThat(actual).isNotNull(),
        () -> assertThat(actual.getName()).isEqualTo(만냥치킨_2마리().getName())
    );
  }

  @Test
  @DisplayName("메뉴를 등록할 때 메뉴의 가격이 0보다 작으면 예외를 반환한다.")
  void create_fail_negative_price() {
    //given
    final Menu menu = 만냥치킨_2마리();
    menu.setPrice(BigDecimal.valueOf(-1));

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("메뉴를 등록할 때 메뉴그룹이 존재하지 않으면 예외를 반환한다.")
  void create_fail_not_exist_menuGroup() {
    //given
    final Menu menu = 만냥치킨_2마리();
    menu.setMenuGroupId(999L);

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("메뉴를 등록할 때 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
  void create_fail_not_exist_product() {
    //given
    final Menu menu = 만냥치킨_2마리_잘못된_상품();

    //when
    final ThrowingCallable actual = () -> menuService.create(menu);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("메뉴를 등록할 때 메뉴의 가격이 메뉴를 구성하는 상품들의 총 가격보다 크면 예외를 반환한다.")
  void create_fail_over_price() {
    //given
    final Menu menu = 만냥치킨_2마리();
    menu.setPrice(BigDecimal.valueOf(40000));

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
