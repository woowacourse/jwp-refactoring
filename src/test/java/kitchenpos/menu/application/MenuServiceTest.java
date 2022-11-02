package kitchenpos.menu.application;

import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getMenuGroupCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {


    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        final MenuGroupResponse response = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        final MenuCreateRequest request = getMenuCreateRequest(response.getId(), createMenuProductDtos());

        final MenuResponse savedMenu = 메뉴_등록(request);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(request.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(request.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(response.getId()),
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
        final MenuGroupResponse response = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        final List<MenuProductDto> menuProductDtos = List.of(new MenuProductDto(null, 1));
        final MenuCreateRequest request = getMenuCreateRequest(response.getId(), menuProductDtos);

        assertThatThrownBy(() -> 메뉴_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        final MenuGroupResponse response = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        final MenuCreateRequest request = getMenuCreateRequest(response.getId(), createMenuProductDtos());
        메뉴_등록(request);

        final List<MenuResponse> menus = menuService.list();

        assertThat(menus).hasSize(1);
    }
}
