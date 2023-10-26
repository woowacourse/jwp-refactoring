package kitchenpos.menu.application;

import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.response.MenuQueryResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.persistence.MenuDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ServiceIntegrateTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuDao menuDao;

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create_success() {
        //given, when
        final MenuQueryResponse savedMenu = menuService.create(getMenu(BigDecimal.valueOf(19000), 1L));
        final Menu actual = menuDao.findById(savedMenu.getId()).get().toMenu();

        //then
        Assertions.assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("만냥치킨+만냥치킨")
        );
    }

    private MenuCreateRequest getMenu(final BigDecimal price, final Long menuGroupId) {
        final MenuProductCreateRequest menuProduct = new MenuProductCreateRequest(1L, 2L);

        return new MenuCreateRequest("만냥치킨+만냥치킨", price, menuGroupId, List.of(menuProduct));
    }


    @Test
    @DisplayName("메뉴를 등록할 때 메뉴의 가격이 0보다 작으면 예외를 반환한다.")
    void create_fail_negative_price() {
        //given
        final MenuCreateRequest menu = getMenu(BigDecimal.valueOf(-1), 1L);

        //when
        final ThrowingCallable actual = () -> menuService.create(menu);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 메뉴그룹이 존재하지 않으면 예외를 반환한다.")
    void create_fail_not_exist_menuGroup() {
        //given
        final MenuCreateRequest menu = getMenu(BigDecimal.valueOf(19000), 999L);

        //when
        final ThrowingCallable actual = () -> menuService.create(menu);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
    void create_fail_not_exist_product() {
        //given
        final MenuCreateRequest menu = getWrongMenu();

        //when
        final ThrowingCallable actual = () -> menuService.create(menu);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    private MenuCreateRequest getWrongMenu() {
        final MenuProductCreateRequest menuProduct = new MenuProductCreateRequest(999L, 2L);

        return new MenuCreateRequest("만냥치킨+만냥치킨", BigDecimal.valueOf(19000), 1L, List.of(menuProduct));
    }

    @Test
    @DisplayName("메뉴를 등록할 때 메뉴의 가격이 메뉴를 구성하는 상품들의 총 가격보다 크면 예외를 반환한다.")
    void create_fail_over_price() {
        //given
        final MenuCreateRequest menu = getMenu(BigDecimal.valueOf(40000), 1L);

        //when
        final ThrowingCallable actual = () -> menuService.create(menu);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 메뉴 목록을 조회할 수 있다.")
    void list_success() {
        // given, when
        final List<MenuQueryResponse> actual = menuService.list();

        //then
        assertThat(actual).hasSize(6);
    }
}
