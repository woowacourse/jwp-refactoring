package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.support.domain.MenuGroupTestSupport;
import kitchenpos.application.support.domain.MenuTestSupport;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProducts;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    MenuGroupRepository menuGroupRepository;
    @Mock
    MenuProductRepository menuProductRepository;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    MenuService target;

    @DisplayName("메뉴를 생성하면 DB에 저장해서 반환한다.")
    @Test
    void create() {
        //given
        final MenuTestSupport.Builder builder = MenuTestSupport.builder();
        final Menu menu = builder.build();
        final MenuCreateRequest request = builder.buildToMenuCreateRequest();
        final MenuProducts menuProducts = menu.getMenuProducts();
        final MenuGroup menuGroup = MenuGroupTestSupport.builder().build();

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(anyLong())).willReturn(
                Optional.of(menuProducts.getValue().get(0).getProduct()));

        given(menuRepository.save(any(Menu.class))).willReturn(menu);
        //when

        //then
        assertDoesNotThrow(() -> target.create(request));
    }

    @DisplayName("메뉴의 가격이 음수이면 예외처리한다.")
    @Test
    void create_fail_price_minus() {
        //given
        final BigDecimal price = new BigDecimal("-1");
        final MenuCreateRequest request = MenuTestSupport.builder().price(price).buildToMenuCreateRequest();
        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 메뉴 그룹에 포함되지 않으면 예외처리한다.")
    @Test
    void create_fail_not_in_menuGroup() {
        //given
        final MenuCreateRequest request = MenuTestSupport.builder().menuGroup(null).buildToMenuCreateRequest();

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력받은 메뉴의 가격은 메뉴 상품들의 가격보다 비싸면 안된다.")
    @Test
    void create_fail_price_greaterThan_product_price() {
        //given
        final BigDecimal price = new BigDecimal(Long.MAX_VALUE);
        final MenuTestSupport.Builder builder = MenuTestSupport.builder().price(price);
        final MenuCreateRequest request = builder.buildToMenuCreateRequest();

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 메뉴를 조회한다.")
    @Test
    void list() {
        //given
        final Menu menu1 = MenuTestSupport.builder().build();
        final Menu menu2 = MenuTestSupport.builder().build();

        final List<Menu> menus = List.of(menu1, menu2);
        given(menuRepository.findAll()).willReturn(menus);
        for (Menu menu : menus) {
            given(menuProductRepository.findAllByMenuId(menu.getId())).willReturn(menu.getMenuProducts().getValue());
        }

        //when
        final List<MenuResponse> result = target.list();

        //then
        assertThat(result)
                .extracting(MenuResponse::getName)
                .contains(menu1.getName(), menu2.getName());
    }
}
