package kitchenpos.menu_group.application;

import static kitchenpos.fixture.MenuGroupFixture.순살파닭두마리메뉴_DTO;
import static kitchenpos.fixture.MenuGroupFixture.한마리메뉴_DTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.ServiceIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("menuGroup을 생성하는 기능")
    void create() {
        //when
        final MenuGroupDto savedMenuGroupDto = menuGroupService.create(순살파닭두마리메뉴_DTO());

        //then
        Assertions.assertThat(menuGroupService.list())
            .usingRecursiveFieldByFieldElementComparator()
            .contains(savedMenuGroupDto);
    }

    @Test
    @DisplayName("menuGroup 전체를 조회하는 기능")
    void list() {
        final List<MenuGroupDto> menuGroupDtos = Stream.of(
                한마리메뉴_DTO(), 한마리메뉴_DTO(), 한마리메뉴_DTO())
            .map(menuGroupService::create)
            .collect(Collectors.toList());

        final List<MenuGroupDto> foundMenuGruopDtos = menuGroupService.list();

        Assertions.assertThat(foundMenuGruopDtos)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
            .containsExactlyInAnyOrderElementsOf(menuGroupDtos);
    }
}
