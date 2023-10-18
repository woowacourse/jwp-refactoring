package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.순살파닭두마리메뉴_DTO;
import static kitchenpos.fixture.MenuFixture.한마리메뉴_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.dto.MenuGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Test
    @DisplayName("menuGroup을 생성하는 기능")
    void create() {
        //when
        final MenuGroupDto savedMenuGroupDto = menuGroupService.create(순살파닭두마리메뉴_DTO());

        //then
        assertThat(menuGroupService.list())
            .usingRecursiveFieldByFieldElementComparator()
            .contains(savedMenuGroupDto);
    }

    @Test
    @DisplayName("menuGroup 전체를 조회하는 기능")
    void list() {
        final List<MenuGroupDto> menuGroupDtos = Stream.of(한마리메뉴_DTO(), 한마리메뉴_DTO(), 한마리메뉴_DTO())
            .map(menuGroupService::create)
            .collect(Collectors.toList());

        final List<MenuGroupDto> foundMenuGruopDtos = menuGroupService.list();

        assertThat(foundMenuGruopDtos)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
            .containsExactlyInAnyOrderElementsOf(menuGroupDtos);
    }
}
