package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.menu.supports.MenuProductFixture;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Test
        void 메뉴_그룹은_DB에_존재해야한다() {
            // given
            Long menuGroupId = 1L;
            MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(1000), menuGroupId, List.of());

            given(menuGroupRepository.findByIdOrThrow(menuGroupId))
                .willThrow(new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    @Nested
    class 메뉴_전체_조회 {

        @Test
        void 전체_메뉴를_조회한다() {
            // given
            List<List<MenuProduct>> menuProducts = List.of(
                List.of(
                    MenuProductFixture.fixture().seq(1L).build(),
                    MenuProductFixture.fixture().seq(2L).build()
                ),
                List.of(
                    MenuProductFixture.fixture().seq(3L).build(),
                    MenuProductFixture.fixture().seq(4L).build()
                )
            );

            List<Menu> menus = List.of(
                MenuFixture.fixture().id(1L).menuProducts(menuProducts.get(0)).build(),
                MenuFixture.fixture().id(2L).menuProducts(menuProducts.get(1)).build()
            );

            given(menuRepository.findAllWithFetch())
                .willReturn(menus);

            // when
            List<MenuResponse> actual = menuService.findAll();

            // then
            assertThat(actual.stream().map(MenuResponse::getId))
                .containsExactly(1L, 2L);
        }
    }
}
