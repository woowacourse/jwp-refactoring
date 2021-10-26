package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("메뉴 서비스 테스트")
@MockitoSettings
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuDao menuDao;

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴의 가격이 음수인 경우")
    @Test
    void createFailedWhenPriceIsNegative() {
        // given
        Menu menu = new Menu("후라이드+후라이드", -1, 1L);

        // when - then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(never())
                .existsById(anyLong());
        then(productDao).should(never())
                .findById(anyLong());
        then(menuDao).should(never())
                .save(menu);
    }

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 그룹 Id가 존재하지 않는 경우")
    @Test
    void createFailedWhenMenuGroupIdNotFound() {
        // given
        Menu menu = new Menu("후라이드+후라이드", 19000, 1L);

        given(menuGroupDao.existsById(1L)).willReturn(false);

        // when - then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(times(1))
                .existsById(anyLong());
        then(productDao).should(never())
                .findById(anyLong());
        then(menuDao).should(never())
                .save(menu);
    }

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 상품이 존재하지 않는 경우")
    @Test
    void createFailedWhenMenuProductNotFound() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(-1L);
        menuProduct.setQuantity(2);

        Menu menu = new Menu("후라이드+후라이드", 19000, 1L, singletonList(menuProduct));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(-1L)).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(times(1))
                .existsById(1L);
        then(productDao).should(times(1))
                .findById(-1L);
        then(menuDao).should(never())
                .save(menu);
    }

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 상품과 수량을 곱한 값보다 메뉴에 등록할 가격이 더 비싼 경우")
    @Test
    void createFailedWhenPriceIsBigger() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        Menu menu = new Menu("후라이드+후라이드", 19000, 1L, singletonList(menuProduct));

        given(menuGroupDao.existsById(1L)).willReturn(true);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(1000));
        given(productDao.findById(1L)).willReturn(Optional.of(product));


        // product.getPrice() => 1000
        // menuProduct.getQuantity() => 2
        // sum => 2000
        // price => 19000
        // 후라이드+후라이드는 19000원에 팔면서, 사실 후라이드 2개 사면 2000원인 경우
        // when - then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(times(1))
                .existsById(1L);
        then(productDao).should(times(1))
                .findById(1L);
        then(menuDao).should(never())
                .save(menu);
    }
}
