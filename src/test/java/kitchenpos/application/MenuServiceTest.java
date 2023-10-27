package kitchenpos.application;

import kitchenpos.order.menu.application.MenuService;
import kitchenpos.order.menu.domain.repository.MenuRepository;
import kitchenpos.order.menu.ui.request.MenuProductRequest;
import kitchenpos.order.menu.ui.request.MenuRequest;
import kitchenpos.order.menu.ui.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuRepository menuRepository;

    @Test
    void 메뉴를_저장한다() {
        final MenuRequest menu = new MenuRequest(
                "메뉴",
                BigDecimal.valueOf(10000),
                1L,
                List.of(new MenuProductRequest(1L, 100L))
        );

        final MenuResponse created = menuService.create(menu);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 가격이_음수면_예외를_발생한다() {
        final MenuRequest menu = new MenuRequest(
                "메뉴",
                BigDecimal.valueOf(-10000),
                1L,
                List.of(new MenuProductRequest(1L, 100L))
        );

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_발생한다() {
        final MenuRequest menu = new MenuRequest(
                "메뉴",
                BigDecimal.valueOf(10000),
                100L,
                List.of(new MenuProductRequest(1L, 100L))
        );

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품을_가지고_있으면_예외를_발생한다() {
        final MenuRequest menu = new MenuRequest(
                "메뉴",
                BigDecimal.valueOf(10000),
                100L,
                List.of(new MenuProductRequest(100L, 100L))
        );

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_제품의_가격과_일치하지_않으면_예외를_빨생한다() {
        final MenuRequest menu = new MenuRequest(
                "메뉴",
                BigDecimal.valueOf(1000000),
                100L,
                List.of(new MenuProductRequest(1L, 100L))
        );

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회힌다() {
        final List<MenuResponse> expected = menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        final List<MenuResponse> actual = menuService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("menuProducts")
                .isEqualTo(expected);
    }
}
