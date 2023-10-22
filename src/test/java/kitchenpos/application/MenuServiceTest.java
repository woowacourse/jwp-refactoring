package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.MenuException.NotExistsProductException;
import kitchenpos.domain.exception.MenuGroupException.NotExistsMenuGroupException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    private Menu menu;
    private Product product1;
    private Product product2;
    private MenuGroup menuGroup;

    @BeforeEach
    void init() {
        product1 = Product.of("kong", BigDecimal.valueOf(1000));
        product2 = Product.of("wuga", BigDecimal.valueOf(5000));
        product1.setId(1L);
        product2.setId(2L);

        MenuProduct menuProduct1 = new MenuProduct(product1, 10);
        MenuProduct menuProduct2 = new MenuProduct(product2, 3);

        menuGroup = new MenuGroup("menuGroup1");

        this.menu = Menu.of("menu", BigDecimal.valueOf(25000), menuGroup, List.of(menuProduct1, menuProduct2));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void create_success() {
        when(menuGroupRepository.getById(1L)).thenReturn(menuGroup);
        when(productRepository.findAllById(anyList())).thenReturn(List.of(product1, product2));
        when(menuRepository.save(any())).thenReturn(menu);

        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 10));
        menuProductRequests.add(new MenuProductRequest(2L, 3));
        MenuRequest menuRequest = new MenuRequest("kong", BigDecimal.valueOf(10000), 1L, menuProductRequests);

        menuService.create(menuRequest);
    }

    @Test
    @DisplayName("메뉴는 현재 존재하는 메뉴 그룹에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_menuGroup() {
        when(menuGroupRepository.getById(1L)).thenThrow(new NotExistsMenuGroupException(1L));

        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 10));
        menuProductRequests.add(new MenuProductRequest(2L, 3));
        MenuRequest menuRequest = new MenuRequest("kong", BigDecimal.valueOf(10000), 1L, menuProductRequests);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotExistsMenuGroupException.class);
    }

    @Test
    @DisplayName("메뉴의 상품 목록이 현재 존재하는 상품에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_products() {
        when(menuGroupRepository.getById(1L)).thenReturn(menuGroup);
        when(productRepository.findAllById(anyList())).thenReturn(List.of(product1));

        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 10));
        menuProductRequests.add(new MenuProductRequest(2L, 3));
        MenuRequest menuRequest = new MenuRequest("kong", BigDecimal.valueOf(10000), 1L, menuProductRequests);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotExistsProductException.class);
    }

    @Test
    @DisplayName("현재 저장된 메뉴 목록을 확인할 수 있다. - 메뉴가 0개일 때")
    void list_success1() {
        menuService.list();

        verify(menuRepository, times(1)).findAll();
    }
}
