package kitchenpos.domain;

import kitchenpos.application.MenuService;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.MenuFixture.REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

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
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록한다() {
            // given
            CreateMenuRequest request = REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
            MenuProduct menuProduct = MENU_PRODUCT.후라이드_치킨_1마리();
            given(menuDao.save(any(Menu.class)))
                    .willReturn(MENU.후라이드_치킨_16000원_1마리());
            given(menuGroupDao.existsById(any()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.of(ProductFixture.PRODUCT.후라이드_치킨()));
            given(menuProductDao.save(any()))
                    .willReturn(menuProduct);

            // when
            CreateMenuResponse result = menuService.create(request);
            List<MenuProductResponse> menuProducts = result.getMenuProducts();

            // then
            assertSoftly(softly -> {
                softly.assertThat(result.getName()).isEqualTo(request.getName());
                softly.assertThat(result.getPrice()).isEqualTo(request.getPrice());
                softly.assertThat(result.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
                softly.assertThat(menuProducts.size()).isEqualTo(request.getMenuProducts().size());
                softly.assertThat(menuProducts.get(0).getProductId()).isEqualTo(request.getMenuProducts().get(0).getProductId());
                softly.assertThat(menuProducts.get(0).getQuantity()).isEqualTo(request.getMenuProducts().get(0).getQuantity());
            });
        }

        @ParameterizedTest(name = "메뉴의 가격이 {0}이면 예외")
        @ValueSource(longs = {-1, -100})
        void 메뉴의_가격이_잘못되면_예외(Long price) {
            // given
            CreateMenuRequest menu = REQUEST.후라이드_치킨_16000원_1마리_등록_요청(price);

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_Null이면_예외() {
            CreateMenuRequest menu = REQUEST.후라이드_치킨_16000원_1마리_등록_요청(null);

            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외() {
            // given
            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(false);
            CreateMenuRequest menu = REQUEST.후라이드_치킨_16000원_1마리_등록_요청();

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_예외() {
            // given
            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.empty());
            CreateMenuRequest menu = REQUEST.후라이드_치킨_16000원_1마리_등록_요청();

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "메뉴가 {0}원이고 상품이 {1}원이면 예외")
        @CsvSource(value = {"17000,16999", "17000,10000", "17000,1000", "17000,1", "17000,0", "17000,-1", "17000,-100"})
        void 메뉴_가격이_상품들의_가격_합보다_크면_예외(Long menuPrice, Long productPrice) {
            // given
            CreateMenuRequest menu = REQUEST.후라이드_치킨_16000원_1마리_등록_요청(menuPrice);
            Product product = ProductFixture.PRODUCT.후라이드_치킨(productPrice);

            given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
            given(productDao.findById(anyLong()))
                    .willReturn(Optional.of(product));

            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_목록_조회 {

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            Menu menu = MENU.후라이드_치킨_16000원_1마리();
            MenuProduct menuProduct = MENU_PRODUCT.후라이드_치킨_1마리();
            given(menuDao.findAll())
                    .willReturn(List.of(menu));
            given(menuProductDao.findAllByMenuId(anyLong()))
                    .willReturn(List.of(menuProduct));

            // when
            List<MenuResponse> result = menuService.list();

            // then
            assertSoftly(softly -> {
                MenuResponse menuResponse = result.get(0);
                softly.assertThat(menuResponse.getId()).isEqualTo(menu.getId());
                softly.assertThat(menuResponse.getName()).isEqualTo(menu.getName());
                softly.assertThat(menuResponse.getPrice()).isEqualTo(menu.getPrice());
                softly.assertThat(menuResponse.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
                softly.assertThat(menuResponse.getMenuProducts().size()).isEqualTo(1);
                softly.assertThat(menuResponse.getMenuProducts().get(0).getProductId()).isEqualTo(menuProduct.getProductId());
                softly.assertThat(menuResponse.getMenuProducts().get(0).getQuantity()).isEqualTo(menuProduct.getQuantity());
            });
        }
    }
}
