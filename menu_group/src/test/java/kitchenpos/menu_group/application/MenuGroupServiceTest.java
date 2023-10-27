package kitchenpos.menu_group.application;

import static kitchenpos.support.fixture.MenuFixture.순살파닭두마리메뉴_DTO;
import static kitchenpos.support.fixture.MenuFixture.한마리메뉴_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.menu_group.application.MenuGroupDto;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Test
    @DisplayName("menuGroup을 생성하는 기능")
    void create() {
        //when
        final MenuGroupDto savedMenuGroupDto = menuGroupService.create(MenuFixture.순살파닭두마리메뉴_DTO());

        //then
        assertThat(menuGroupService.list())
            .usingRecursiveFieldByFieldElementComparator()
            .contains(savedMenuGroupDto);
    }

    @Test
    @DisplayName("menuGroup 전체를 조회하는 기능")
    void list() {
        final List<MenuGroupDto> menuGroupDtos = Stream.of(
                MenuFixture.한마리메뉴_DTO(), MenuFixture.한마리메뉴_DTO(), MenuFixture.한마리메뉴_DTO())
            .map(menuGroupService::create)
            .collect(Collectors.toList());

        final List<MenuGroupDto> foundMenuGruopDtos = menuGroupService.list();

        Assertions.assertThat(foundMenuGruopDtos)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
            .containsExactlyInAnyOrderElementsOf(menuGroupDtos);
    }
}
