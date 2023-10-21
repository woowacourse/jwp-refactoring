package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("새로운 메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드 양념 두마리 세트");
        menu.setPrice(BigDecimal.valueOf(30000));

        final MenuGroup menuGroup = new MenuGroup(10L, "치킨");

        menu.setMenuGroupId(menuGroup.getId());

        final Product product1 = new Product(100L, "후라이드 치킨", BigDecimal.valueOf(16000));
        final Product product2 = new Product(101L, "양념 치킨", BigDecimal.valueOf(17000));

        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(100L);
        menuProduct1.setQuantity(1);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setMenuId(1L);
        menuProduct2.setProductId(101L);
        menuProduct2.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.findById(menuProduct1.getProductId()))
                .willReturn(Optional.of(product1));
        given(productRepository.findById(menuProduct2.getProductId()))
                .willReturn(Optional.of(product2));

        given(menuDao.save(any()))
                .willReturn(menu);

        given(menuProductDao.save(menuProduct1))
                .willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2))
                .willReturn(menuProduct2);

        // when & then
        assertThat(menuService.create(menu)).isEqualTo(menu);
        then(menuGroupRepository).should(times(1)).existsById(anyLong());
        then(productRepository).should(times(2)).findById(anyLong());
        then(menuDao).should(times(1)).save(any());
        then(menuProductDao).should(times(2)).save(any());
    }

    @DisplayName("메뉴의 가격이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceNull() {
        // given
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드 양념 두마리 세트");
        menu.setPrice(null);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0 미만이면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceLessThanZero() {
        // given
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드 양념 두마리 세트");
        menu.setPrice(BigDecimal.valueOf(-1));
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 올바르지 않습니다.");
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenMenuGroupNotExist() {
        // given
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드 양념 두마리 세트");
        menu.setPrice(BigDecimal.valueOf(30000));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("메뉴가 가진 메뉴 상품들 중 상품이 하나라도 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenProductNotExist() {
        // given
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드 양념 두마리 세트");
        menu.setPrice(BigDecimal.valueOf(30000));

        final MenuGroup menuGroup = new MenuGroup(10L, "치킨");

        menu.setMenuGroupId(menuGroup.getId());

        final Product product1 = new Product(100L, "후라이드 치킨", BigDecimal.valueOf(16000));
        final Product product2 = new Product(101L, "양념 치킨", BigDecimal.valueOf(17000));

        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(1);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setMenuId(1L);
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @DisplayName("메뉴가 가진 메뉴 상품들 중 상품이 하나라도 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenPriceIncorrect() {
        // given
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드 양념 두마리 세트");
        menu.setPrice(BigDecimal.valueOf(40000));

        final MenuGroup menuGroup = new MenuGroup(10L, "치킨");

        menu.setMenuGroupId(menuGroup.getId());

        final Product product1 = new Product(100L, "후라이드 치킨", BigDecimal.valueOf(16000));
        final Product product2 = new Product(101L, "양념 치킨", BigDecimal.valueOf(17000));

        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(100L);
        menuProduct1.setQuantity(1);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setMenuId(1L);
        menuProduct2.setProductId(101L);
        menuProduct2.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.findById(menuProduct1.getProductId()))
                .willReturn(Optional.of(product1));
        given(productRepository.findById(menuProduct2.getProductId()))
                .willReturn(Optional.of(product2));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품 가격들의 합보다 큽니다.");
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Menu menu1 = new Menu();
        menu1.setId(1L);
        final Menu menu2 = new Menu();
        menu2.setId(2L);

        final List<Menu> menus = List.of(menu1, menu2);

        final List<MenuProduct> menuProducts1 = List.of(new MenuProduct());
        final List<MenuProduct> menuProducts2 = List.of(new MenuProduct(), new MenuProduct());

        given(menuDao.findAll())
                .willReturn(menus);
        given(menuProductDao.findAllByMenuId(1L))
                .willReturn(menuProducts1);
        given(menuProductDao.findAllByMenuId(2L))
                .willReturn(menuProducts2);

        // when & then
        assertThat(menuService.list()).isEqualTo(menus);
    }
}
