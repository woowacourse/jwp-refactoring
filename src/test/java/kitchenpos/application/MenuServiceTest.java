package kitchenpos.application;

import static kitchenpos.DomainFixture.getMenuGroup;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {


    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final MenuCreateRequest request = getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos());

        final Menu savedMenu = 메뉴_등록(request);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(request.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(request.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴를 등록한다. - 메뉴 그룹이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchMenuGroup() {
        final MenuCreateRequest request = getMenuCreateRequest(null, createMenuProductDtos());

        assertThatThrownBy(() -> 메뉴_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchProduct() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final List<MenuProductDto> menuProductDtos = List.of(new MenuProductDto(null, 1));
        final MenuCreateRequest request = getMenuCreateRequest(menuGroup.getId(), menuProductDtos);

        assertThatThrownBy(() -> 메뉴_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final MenuCreateRequest request = getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos());
        메뉴_등록(request);

        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }
}
