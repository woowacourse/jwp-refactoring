package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

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

    @Test
    void 메뉴이름_가격_메뉴그룹ID_메뉴상품리스트를_받아서_메뉴_정보를_등록할_수_있다() {
        //given
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Product 상품_강정치킨 = ProductFixture.상품_강정치킨();
        MenuProduct 후라이드_강정치킨 = MenuProductFixture.후라이드_강정치킨();

        given(menuGroupDao.findById(추천메뉴.getId())).willReturn(Optional.of(추천메뉴));
        given(productDao.findById(상품_강정치킨.getId())).willReturn(Optional.of(상품_강정치킨));
        given(menuDao.save(any(Menu.class))).willReturn(메뉴_후라이드);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(후라이드_강정치킨);

        MenuProductRequest menuProductRequest = new MenuProductRequest(상품_강정치킨.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest(
                메뉴_후라이드.getName(),
                메뉴_후라이드.getPrice(),
                추천메뉴.getId(),
                List.of(menuProductRequest)
        );

        //when
        MenuResponse response = menuService.create(menuRequest);

        //then
        assertThat(response.getId()).isEqualTo(메뉴_후라이드.getId());
        assertThat(response.getName()).isEqualTo(메뉴_후라이드.getName());
        assertThat(response.getPrice()).isEqualTo(메뉴_후라이드.getPrice());
        assertThat(response.getMenuGroupId()).isEqualTo(추천메뉴.getId());
        assertThat(response.getMenuProductDtos().get(0).getMenuId()).isEqualTo(메뉴_후라이드.getId());
        assertThat(response.getMenuProductDtos().get(0).getProductId()).isEqualTo(상품_강정치킨.getId());
    }

    @Test
    void 메뉴_가격이_입력되지_않으면_예외처리한다() {
        //given
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Product 상품_강정치킨 = ProductFixture.상품_강정치킨();

        MenuProductRequest menuProductRequest = new MenuProductRequest(상품_강정치킨.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest(
                메뉴_후라이드.getName(),
                null,
                추천메뉴.getId(),
                List.of(menuProductRequest)
        );

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0원_미만으로_입력되면_예외처리한다() {
        //given
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Product 상품_강정치킨 = ProductFixture.상품_강정치킨();

        MenuProductRequest menuProductRequest = new MenuProductRequest(상품_강정치킨.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest(
                메뉴_후라이드.getName(),
                BigDecimal.valueOf(0),
                추천메뉴.getId(),
                List.of(menuProductRequest)
        );

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_ID가_존재하지_않는_것이면_예외처리한다() {
        //given
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Product 상품_강정치킨 = ProductFixture.상품_강정치킨();

        MenuProductRequest menuProductRequest = new MenuProductRequest(상품_강정치킨.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest(
                메뉴_후라이드.getName(),
                메뉴_후라이드.getPrice(),
                -1L,
                List.of(menuProductRequest)
        );

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 각_메뉴_상품들의_가격과_수량을_곱한_값의_합보다_메뉴_가격이_높으면_예외처리한다() {
        //given
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Product 상품_강정치킨 = ProductFixture.상품_강정치킨();

        given(productDao.findById(상품_강정치킨.getId())).willReturn(Optional.of(상품_강정치킨));

        MenuProductRequest menuProductRequest = new MenuProductRequest(상품_강정치킨.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest(
                메뉴_후라이드.getName(),
                BigDecimal.valueOf(50000),
                추천메뉴.getId(),
                List.of(menuProductRequest)
        );

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_전체_메뉴_정보를_조회할_수_있다() {
        //given
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        MenuProduct 후라이드_강정치킨 = MenuProductFixture.후라이드_강정치킨();

        given(menuDao.findAll()).willReturn(List.of(메뉴_후라이드));
        given(menuProductDao.findAllByMenuId(메뉴_후라이드.getId())).willReturn(
                List.of(후라이드_강정치킨));

        //when
        List<MenuResponse> result = menuService.list();

        //then
        assertThat(result).hasSize(1);
    }
}
