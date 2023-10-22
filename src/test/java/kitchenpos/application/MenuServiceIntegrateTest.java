package kitchenpos.application;

import kitchenpos.application.test.ServiceIntegrateTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.domain.fixture.MenuGroupFixture.인기_메뉴_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuServiceIntegrateTest extends ServiceIntegrateTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class 메뉴를_생성한다 {

        @Test
        void 메뉴를_생성한다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(인기_메뉴_생성());
            MenuCreateRequest request = new MenuCreateRequest("치킨", BigDecimal.valueOf(20000), menuGroup.getId());

            // when, then
            assertDoesNotThrow(() -> menuService.create(request));
        }

        @Test
        void 메뉴의_가격이_null이면_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(인기_메뉴_생성());
            MenuCreateRequest request = new MenuCreateRequest("치킨", null, menuGroup.getId());

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(request));
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(인기_메뉴_생성());
            MenuCreateRequest request = new MenuCreateRequest("치킨", BigDecimal.valueOf(-100), menuGroup.getId());

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(request));
        }

        @Test
        void 메뉴의_MenuGroupId가_존재하지_않는다면_예외가_발생한다() {
            // given
            MenuCreateRequest request = new MenuCreateRequest("치킨", BigDecimal.valueOf(1000), 1L);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(request));
        }

    }

    @Nested
    class 메뉴_목록을_조회한다 {

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = menuGroupRepository.save(인기_메뉴_생성());
            Menu menu = new Menu("치킨", BigDecimal.valueOf(20000), menuGroup);
            menuRepository.save(menu);
        }

        @Test
        void 메뉴_목록을_조회한다() {
            // when
            List<MenuResponse> menus = menuService.list();

            // then
            Assertions.assertAll(
                    () -> assertThat(menus).hasSize(1),
                    () -> assertThat(menus).extracting(MenuResponse::getName)
                            .contains("치킨")
            );
        }

    }

}
