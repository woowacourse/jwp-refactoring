package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.MenuException.NotExistsMenuGroupException;
import kitchenpos.domain.exception.MenuException.NotExistsProductException;
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
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void init() {
        Product kong = Product.of("kong", BigDecimal.valueOf(1000));
        Product wuga = Product.of("wuga", BigDecimal.valueOf(5000));
        kong.setId(1L);
        wuga.setId(2L);

        menuProduct1 = new MenuProduct(kong, 10);
        menuProduct2 = new MenuProduct(wuga, 3);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        menuGroup.setId(1L);

        this.menu = Menu.of("menu", BigDecimal.valueOf(25000), menuGroup, List.of(menuProduct1, menuProduct2));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다. - 메뉴가 생성될 때 메뉴 상품들도 함께 저장된다.")
    void create_success1() {
        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.countByIdIn(List.of(1L, 2L))).thenReturn(2L);

        when(menuRepository.save(menu)).thenReturn(new Menu());

        menuService.create(menu);

        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다. - 메뉴 가격이 0원이고, 상품 가격들도 0원이면 메뉴를 생성한다.")
    void create_success2() {
        menu.setPrice(BigDecimal.valueOf(0));

        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.countByIdIn(List.of(1L, 2L))).thenReturn(2L);

        when(menuRepository.save(menu)).thenReturn(new Menu());

        menuService.create(menu);

        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    @DisplayName("메뉴는 현재 존재하는 메뉴 그룹에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_menuGroup() {
        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NotExistsMenuGroupException.class);
    }

    @Test
    @DisplayName("메뉴의 상품 목록이 현재 존재하는 상품에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_products() {
        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.countByIdIn(List.of(1L, 2L))).thenReturn(1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NotExistsProductException.class);
    }

    @Test
    @DisplayName("현재 저장된 메뉴 목록을 확인할 수 있다. - 메뉴가 0개일 때")
    void list_success1() {
        menuService.list();

        verify(menuRepository, times(1)).findAll();
    }
}
