package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrationTest {

  @Autowired
  private MenuGroupService menuGroupService;

  @Autowired
  private MenuGroupRepository menuGroupRepository;

  @Test
  @DisplayName("create() : 메뉴 그룹을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final MenuGroup menuGroup = MenuGroupFixture.createMenuGroup();

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
    //given
    menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
    menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
    menuGroupRepository.save(MenuGroupFixture.createMenuGroup());

    //when
    final List<MenuGroup> menuGroups = menuGroupService.list();

    //then
    assertEquals(3, menuGroups.size());
  }
}
