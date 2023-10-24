package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.MenuProductFixture;
import kitchenpos.supports.ProductFixture;
import org.assertj.core.api.AssertionsForClassTypes;
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
        void 메뉴의_가격은_null일_수_없다() {
            // given
            BigDecimal price = null;
            MenuCreateRequest request = new MenuCreateRequest("메뉴", price, 1L, List.of());

            // when
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴 가격입니다.");
        }

        @Test
        void 메뉴의_가격은_음수일_수_없다() {
            // given
            BigDecimal price = new BigDecimal(-1000);
            MenuCreateRequest request = new MenuCreateRequest("메뉴", price, 1L, List.of());

            // when
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴 가격입니다.");
        }

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

        @Test
        void 메뉴_상품의_상품은_모두_DB에_존재해야한다() {
            // given
            Long menuGroupId = 1L;
            MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(1000), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
                ));
            MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();

            given(menuGroupRepository.findByIdOrThrow(menuGroupId))
                .willReturn(menuGroup);
            given(menuRepository.save(any(Menu.class)))
                .willReturn(MenuFixture.fixture().menuGroup(menuGroup).build());
            given(productRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(List.of(ProductFixture.fixture().id(1L).build()));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
        }

        @Test
        void 메뉴_가격이_상품의_가격_총합보다_크면_예외() {
            // given
            int menuPrice = 9000;
            Long menuGroupId = 1L;
            MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(menuPrice), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 2L)
                ));
            MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();
            List<Product> products = List.of(
                ProductFixture.fixture().id(1L).price(2000).build(),
                ProductFixture.fixture().id(2L).price(3000).build()
            );

            given(menuGroupRepository.findByIdOrThrow(menuGroupId))
                .willReturn(menuGroup);
            given(menuRepository.save(any(Menu.class)))
                .willReturn(new Menu(1L, request.getName(), new Price(request.getPrice()), menuGroup));
            given(productRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(products);

            // when
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 상품의 가격 총 합보다 클 수 없습니다.");
        }

        @Test
        void 이름_가격_메뉴그룹id_메뉴상품들을_입력받아_생성한다() {
            // given
            int menuPrice = 8000;
            Long menuGroupId = 1L;
            MenuCreateRequest request = new MenuCreateRequest("치킨 콤보 세트", new BigDecimal(menuPrice), menuGroupId,
                List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 2L)
                ));
            MenuGroup menuGroup = MenuGroupFixture.fixture().id(menuGroupId).build();
            List<Product> products = List.of(
                ProductFixture.fixture().id(1L).price(2000).build(),
                ProductFixture.fixture().id(2L).price(3000).build()
            );

            given(menuGroupRepository.findByIdOrThrow(menuGroupId))
                .willReturn(menuGroup);
            given(menuRepository.save(any(Menu.class)))
                .willReturn(new Menu(1L, request.getName(), new Price(request.getPrice()), menuGroup));
            given(productRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(products);

            // when
            MenuResponse actual = menuService.create(request);

            // then
            AssertionsForClassTypes.assertThat(actual.getId())
                .isEqualTo(1L);
        }
    }

    @Nested
    class 메뉴_전체_조회 {

        @Test
        void 전체_메뉴를_조회한다() {
            // given
            List<Menu> menus = List.of(
                MenuFixture.fixture().id(1L).build(),
                MenuFixture.fixture().id(2L).build()
            );
            List<List<MenuProduct>> menuProducts = List.of(
                List.of(
                    MenuProductFixture.fixture().seq(1L).menu(menus.get(0)).build(),
                    MenuProductFixture.fixture().seq(2L).menu(menus.get(0)).build()
                ),
                List.of(
                    MenuProductFixture.fixture().seq(3L).menu(menus.get(1)).build(),
                    MenuProductFixture.fixture().seq(4L).menu(menus.get(1)).build()
                )
            );

            menus.get(0).setUpMenuProducts(menuProducts.get(0));
            menus.get(1).setUpMenuProducts(menuProducts.get(1));

            given(menuRepository.findAllWithFetch())
                .willReturn(menus);

            // when
            List<MenuResponse> actual = menuService.list();

            // then
            assertThat(actual.stream().map(MenuResponse::getId))
                .containsExactly(1L, 2L);
        }
    }
}
