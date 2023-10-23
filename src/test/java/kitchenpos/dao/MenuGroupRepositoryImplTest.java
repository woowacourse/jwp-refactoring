package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.support.JdbcTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupRepositoryImplTest extends JdbcTestHelper {

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepositoryImpl;

  @Test
  @DisplayName("save() : 메뉴 그룹을 생성할 수 있다.")
  void test_save() throws Exception {
    //given
    final MenuGroup menuGroup = MenuGroupFixture.createMenuGroup();
    //when
    final MenuGroup savedMenuGroup = menuGroupRepositoryImpl.save(menuGroup);

    //then
    assertAll(
        () -> assertNotNull(savedMenuGroup.getId()),
        () -> assertThat(savedMenuGroup)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(menuGroup)
    );
  }

  @Test
  @DisplayName("findById() : 메뉴 그룹을 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final MenuGroup menuGroup = menuGroupRepositoryImpl.save(MenuGroupFixture.createMenuGroup());

    //when
    final Optional<MenuGroup> savedMenuGroup = menuGroupRepositoryImpl.findById(menuGroup.getId());

    //then
    assertAll(
        () -> assertTrue(savedMenuGroup.isPresent()),
        () -> Assertions.assertThat(savedMenuGroup.get())
            .usingRecursiveComparison()
            .isEqualTo(menuGroup)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 메뉴 그룹을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final MenuGroup menuGroup1 = menuGroupRepositoryImpl.save(MenuGroupFixture.createMenuGroup());
    final MenuGroup menuGroup2 = menuGroupRepositoryImpl.save(MenuGroupFixture.createMenuGroup());

    //when
    final List<MenuGroup> menuGroups = menuGroupRepositoryImpl.findAll();

    //then
    assertAll(
        () -> assertEquals(2, menuGroups.size()),
        () -> Assertions.assertThat(menuGroups)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(List.of(menuGroup1, menuGroup2))
    );
  }

  @Test
  @DisplayName("existsById() : id를 통해 메뉴 그룹이 존재하는지 확인할 수 있다.")
  void test_existsById() throws Exception {
    //given
    final MenuGroup menuGroup = menuGroupRepositoryImpl.save(MenuGroupFixture.createMenuGroup());
    final long nonExistedId = 3339L;

    //when & then
    assertTrue(menuGroupRepositoryImpl.existsById(menuGroup.getId()));
    assertFalse(menuGroupRepositoryImpl.existsById(nonExistedId));
  }
}
