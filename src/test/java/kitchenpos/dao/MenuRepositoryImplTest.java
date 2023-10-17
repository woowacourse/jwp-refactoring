package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.support.JdbcTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

class MenuRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private MenuRepositoryImpl menuRepository;
  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  private MenuGroup menuGroup;

  @BeforeEach
  void setUp() {
    menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
  }

  @Test
  @DisplayName("save() : 메뉴를 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final Menu2 menu = MenuFixture.createMenu(menuGroup);

    //when
    final Menu2 savedMenu = menuRepository.save(menu);

    //then
    assertAll(
        () -> assertNotNull(savedMenu.getId()),
        () -> assertEquals(0, savedMenu.getPrice().compareTo(menu.getPrice())),
        () -> assertThat(savedMenu.getMenuGroup())
            .usingRecursiveComparison()
            .ignoringFields("id", "price")
            .isEqualTo(menu.getMenuGroup())
    );
  }

  @Test
  @DisplayName("findById() : 메뉴를 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final Menu2 menu = menuRepository.save(MenuFixture.createMenu(menuGroup));

    //when
    final Optional<Menu2> savedMenu = menuRepository.findById(menu.getId());

    //then
    assertAll(
        () -> assertTrue(savedMenu.isPresent()),
        () -> assertThat(savedMenu.get())
            .usingRecursiveComparison()
            .isEqualTo(menu)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 메뉴를 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final Menu2 menu1 = menuRepository.save(MenuFixture.createMenu(menuGroup));
    final Menu2 menu2 = menuRepository.save(MenuFixture.createMenu(menuGroup));

    //when
    final List<Menu2> menus = menuRepository.findAll();

    //then
    assertAll(
        () -> assertEquals(2, menus.size()),
        () -> Assertions.assertThat(menus)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(List.of(menu1, menu2))
    );
  }

  @Test
  @DisplayName("countByIdIn() : 특정 메뉴들이 존재하는 갯수를 확인할 수 있다.")
  void test_countByIdIn() throws Exception {
    //given
    final Menu2 menu1 = menuRepository.save(MenuFixture.createMenu(menuGroup));
    final Menu2 menu2 = menuRepository.save(MenuFixture.createMenu(menuGroup));

    final List<Long> ids = List.of(menu1.getId(), menu2.getId(), 9999L);

    //when
    final long actual = menuRepository.countByIdIn(ids);

    //then
    assertEquals(ids.size() - 1, actual);
  }
}
