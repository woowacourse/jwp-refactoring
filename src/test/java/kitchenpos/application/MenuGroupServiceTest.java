package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrateTest {

  @Autowired
  private MenuGroupService menuGroupService;

  @Test
  @DisplayName("메뉴 그룹을 등록할 수 있다.")
  void create_success() {
    //given, when
    final MenuGroup actual = menuGroupService.create(추천_메뉴());

    //then
    assertThat(actual).isNotNull();

  }

  @Test
  @DisplayName("등록된 메뉴 그룹 목록을 조회할 수 있다.")
  void list_success() {
    // given, when
    final List<MenuGroup> actual = menuGroupService.list();
    final List<String> actualNames = actual.stream()
        .map(MenuGroup::getName)
        .collect(Collectors.toList());

    //then
    Assertions.assertAll(
        () -> assertThat(actual).hasSize(4),
        () -> assertThat(actualNames).containsExactly("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴")
    );
  }
}
