package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("menu의 menuGroup이 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuGroupNotExist_throwsException() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(1L, 3);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final MenuRequest menu = new MenuRequest("메뉴1", BigDecimal.valueOf(1000), 99L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("menu의 menuProduct가 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuProductNotExist_throwsExcpetion() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(99L, 3);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final MenuRequest menu = new MenuRequest("메뉴1", BigDecimal.valueOf(1000), 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @DisplayName("menu 가격이 0보다 작은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceIsNegative_throwsException() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(1L, 3);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final MenuRequest menuRequest = new MenuRequest("메뉴1", BigDecimal.valueOf(-1000), 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }

    @DisplayName("menu 가격이 null인 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceIsNull_throwsException() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(1L, 3);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final MenuRequest menu = new MenuRequest("메뉴1", null, 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }

    @DisplayName("menu의 가격이 menu의 product들의 가격보다 비싼 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceMoreExpensiveThanProducts_throwsException() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(1L, 1);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 1);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final MenuRequest menu = new MenuRequest("메뉴1", BigDecimal.valueOf(40000), 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격과 메뉴 상품의 총 가격은 동일해야합니다.");
    }

    @DisplayName("menu가 정상적으로 저장이 된다.")
    @Test
    void create() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(1L, 1);
        final MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 1);
        final List<MenuProductRequest> menuProductRequests = Arrays.asList(menuProduct1, menuProduct2);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));

        final MenuRequest menuRequest = new MenuRequest("메뉴1", BigDecimal.valueOf(32000), menuGroup.getId(),
                menuProductRequests);

        // when
        final Menu menu = menuService.create(menuRequest);

        // then
        assertThat(menu.getId()).isNotNull();
    }

    @DisplayName("menu들을 조회한다.")
    @Test
    void list() {
        // when
        final List<Menu> menus = menuService.list();

        final List<Long> ids = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        // then
        assertThat(ids).containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
    }
}
