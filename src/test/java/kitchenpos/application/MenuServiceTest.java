package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
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

    private static final Product PRODUCT_1000 = ProductFixture.builder()
        .withId(1L)
        .withPrice(1000L)
        .build();

    private static final Product PRODUCT_500 = ProductFixture.builder()
        .withId(2L)
        .withPrice(500L)
        .build();

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Nested
        class 메뉴_가격_정책 {

            @Test
            void 메뉴_생성_시_가격이_음수라면_예외() {
                // given
                MenuCreateRequest request = new MenuCreateRequest(null, BigDecimal.valueOf(-1000L), null,
                    null);

                // when && then
                assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_생성_시_가격을_보내지_않으면_예외() {
                // given
                MenuCreateRequest request = new MenuCreateRequest(null, null, null,
                    null);

                // when && then
                assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_상품_가격의_총합이_메뉴의_가격보다_높으면_예외() {
                // given
                MenuCreateRequest request = new MenuCreateRequest(
                    null,
                    BigDecimal.valueOf(6600),
                    null,
                    List.of(
                        new MenuProductCreateRequest(1L, 5L),
                        new MenuProductCreateRequest(2L, 3L))
                );

                given(productDao.findById(anyLong()))
                    .willReturn(
                        Optional.of(PRODUCT_1000),
                        Optional.of(PRODUCT_500)
                    );

                // when && then
                assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Test
        void 메뉴_그룹이_저장된_메뉴_그룹이_아니면_예외() {
            // given
            MenuCreateRequest request = new MenuCreateRequest(
                null,
                BigDecimal.valueOf(5400),
                1L,
                List.of(new MenuProductCreateRequest(1L, 5L)));

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(false);

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품을_메뉴_상품에_넣으면_예외() {
            // given
            MenuCreateRequest request = new MenuCreateRequest(
                null,
                BigDecimal.valueOf(5400),
                1L,
                List.of(new MenuProductCreateRequest(1L, 5L)));

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_완료() {
            String menuName = "name";
            BigDecimal menuPrice = BigDecimal.valueOf(5400);
            long menuGroupId = 1L;
            MenuProductCreateRequest firstMenuProductRequest = new MenuProductCreateRequest(1L, 5L);
            MenuProductCreateRequest secondMenuProductRequest = new MenuProductCreateRequest(2L, 3L);
            MenuCreateRequest request = new MenuCreateRequest(
                menuName,
                menuPrice,
                menuGroupId,
                List.of(
                    firstMenuProductRequest,
                    secondMenuProductRequest)
            );

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(anyLong()))
                .willReturn(
                    Optional.of(PRODUCT_1000),
                    Optional.of(PRODUCT_500)
                );

            Long menuId = 1L;
            given(menuDao.save(any()))
                .willReturn(new Menu(
                    menuId,
                    menuName,
                    menuPrice,
                    menuGroupId,
                    new ArrayList<>()
                ));

            given(menuProductDao.save(any()))
                .willReturn(
                    toEntity(1L, menuId, firstMenuProductRequest),
                    toEntity(2L, menuId, secondMenuProductRequest));

            // when
            MenuResponse actual = menuService.create(request);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual.getMenuGroupId()).isEqualTo(menuGroupId);
                assertThat(actual.getMenuProducts()).hasSize(2);
                assertThat(actual.getMenuProducts())
                    .allMatch(menuProduct -> menuProduct.getMenuId().equals(menuId));
            });
        }

        private MenuProduct toEntity(Long seq, Long menuId, MenuProductCreateRequest menuProductCreateRequest) {
            return new MenuProduct(seq, menuId, menuProductCreateRequest.getProductId(),
                menuProductCreateRequest.getQuantity());
        }
    }

    @Test
    void 메뉴_목록_검색() {
        // given
        long menuId = 1L;
        MenuProduct firstMenuProduct = MenuProductFixture.builder()
            .withMenuId(menuId)
            .build();
        MenuProduct secondMenuProduct = MenuProductFixture.builder()
            .withMenuId(menuId)
            .build();

        List<MenuProduct> ofFirstMenu = List.of(firstMenuProduct, secondMenuProduct);
        Menu menu = MenuFixture.builder()
            .withId(menuId)
            .withMenuProducts(ofFirstMenu)
            .build();

        given(menuDao.findAll())
            .willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(anyLong()))
            .willReturn(ofFirstMenu);

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
