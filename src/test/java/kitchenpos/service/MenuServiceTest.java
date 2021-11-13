package kitchenpos.service;

import kitchenpos.application.MenuProductService;
import kitchenpos.application.MenuService;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스 테스트")
class MenuServiceTest extends ServiceTest {

    @InjectMocks
    private MenuService menuService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductService menuProductService;

    private MenuRequest request;
    private MenuProduct menuProduct;
    private List<MenuProductRequest> menuProductRequests;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct(MenuFixture.create(), ProductFixture.create(), 1);
        menuProducts = Collections.singletonList(menuProduct);
        menuProductRequests = Collections.singletonList(new MenuProductRequest(ProductFixture.create().getId(), 1L));

        request = new MenuRequest("이달의치킨", BigDecimal.valueOf(20_000), 1L, menuProductRequests);
    }

    @DisplayName("메뉴 생성 - 성공")
    @Test
    void create() {
        //given
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(MenuGroupFixture.create()));
        when(menuProductService.create(any(), any())).thenReturn(menuProducts);
        when(menuRepository.save(any())).thenReturn(MenuFixture.create());
        //when
        MenuResponse menuResponse = menuService.create(request);
        //then
        assertThat(menuResponse.getId()).isNotNull();
        assertThat(menuResponse.getMenuProducts()).isEqualTo(request.getMenuProductRequests());
    }

    @DisplayName("메뉴 조회 - 성공")
    @Test
    void findAll() {
        //given
        when(menuProductService.findAllByMenuId(anyLong())).thenReturn(menuProducts);
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(MenuFixture.create()));
        //when
        List<MenuResponse> menus = menuService.findAll();
        //then
        assertThat(menus).hasSize(1);
    }
}
