package kitchenpos.application;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.mapper.MenuMapper;
import kitchenpos.domain.mapper.MenuProductMapper;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.MenuFixture.REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuMapper menuMapper;

    @Mock
    private MenuProductMapper menuProductMapper;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;


    @Nested
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록한다() {
            // given
            CreateMenuRequest request = REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
            MenuProduct menuProduct = MENU_PRODUCT.후라이드_치킨_1마리();

            given(menuProductMapper.toMenuProduct(any(MenuProductRequest.class)))
                    .willReturn(menuProduct);
            given(menuMapper.toMenu(any(CreateMenuRequest.class)))
                    .willReturn(MENU.후라이드_치킨_16000원_1마리());
            given(menuRepository.save(any(Menu.class)))
                    .willReturn(MENU.후라이드_치킨_16000원_1마리());
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
    }

    @Nested
    class 메뉴_목록_조회 {

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            Menu menu = MENU.후라이드_치킨_16000원_1마리();
            MenuProduct menuProduct = MENU_PRODUCT.후라이드_치킨_1마리();
            given(menuRepository.findAll())
                    .willReturn(List.of(menu));

            // when
            List<MenuResponse> result = menuService.list();

            // then
            assertSoftly(softly -> {
                MenuResponse menuResponse = result.get(0);
                softly.assertThat(menuResponse.getId()).isEqualTo(menu.getId());
                softly.assertThat(menuResponse.getName()).isEqualTo(menu.getName());
                softly.assertThat(menuResponse.getPrice()).isEqualTo(menu.getPrice());
                softly.assertThat(menuResponse.getMenuGroupId()).isEqualTo(menu.getMenuGroup().getId());
                softly.assertThat(menuResponse.getMenuProducts().size()).isEqualTo(1);
                softly.assertThat(menuResponse.getMenuProducts().get(0).getProductId()).isEqualTo(menuProduct.getProduct().getId());
                softly.assertThat(menuResponse.getMenuProducts().get(0).getQuantity()).isEqualTo(menuProduct.getQuantity());
            });
        }
    }
}
