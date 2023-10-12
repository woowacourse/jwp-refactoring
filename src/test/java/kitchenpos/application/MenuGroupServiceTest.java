package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

  @Autowired
  private MenuGroupService menuGroupService;

  @Test
  @DisplayName("create() : 메뉴 그룹을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final MenuGroup menuGroup = new MenuGroup("menugroup");

    //when
    final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

    //then
    assertAll(
        () -> Assertions.assertNotNull(savedMenuGroup.getId()),
        () -> assertEquals(savedMenuGroup.getName(), menuGroup.getName())
    );
  }

  @Test
  @DisplayName("list() : 모든 메뉴 그룹들을 조회할 수 있다.")
  void test_list() throws Exception {
    //when
    final List<MenuGroup> list = menuGroupService.list();

    //then
    assertEquals(4, list.size());
  }
}
