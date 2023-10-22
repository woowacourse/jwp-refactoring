package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.menu.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.MenuGroupQueryResponse;
import kitchenpos.menu.application.entity.MenuGroupEntity;
import kitchenpos.menu.persistence.MenuGroupDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrateTest {

  @Autowired
  private MenuGroupService menuGroupService;
  @Autowired
  private MenuGroupDao menuGroupDao;

  @Test
  @DisplayName("메뉴 그룹을 등록할 수 있다.")
  void create_success() {
    //given, when
    final String name = "추천 메뉴";
    final MenuGroupQueryResponse savedManuGroup = menuGroupService.create(
        new MenuGroupCreateRequest(name));
    final MenuGroupEntity actual = menuGroupDao.findById(savedManuGroup.getId()).get();

    //then
    Assertions.assertAll(
        () -> assertThat(actual).isNotNull(),
        () -> assertThat(actual.getName()).isEqualTo(name)
    );
  }

  @Test
  @DisplayName("등록된 메뉴 그룹 목록을 조회할 수 있다.")
  void list_success() {
    // given, when
    final List<MenuGroupQueryResponse> actual = menuGroupService.list();
    final List<String> actualNames = actual.stream()
        .map(MenuGroupQueryResponse::getName)
        .collect(Collectors.toList());

    //then
    Assertions.assertAll(
        () -> assertThat(actual).hasSize(4),
        () -> assertThat(actualNames).containsExactly("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴")
    );
  }
}
