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
import java.util.stream.Collectors;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.product.doamin.Product;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("새로운 메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product product1 = new Product(100L, "후라이드 치킨", new Price(BigDecimal.valueOf(16000)));
        final Product product2 = new Product(101L, "양념 치킨", new Price(BigDecimal.valueOf(17000)));

        final MenuGroup menuGroup = new MenuGroup(10L, "치킨");
        final MenuCreateRequest menuRequest = new MenuCreateRequest(
                "후라이드 양념 세트",
                BigDecimal.valueOf(30000),
                menuGroup.getId(),
                List.of(
                        new MenuProductCreateRequest(product1.getId(), 1),
                        new MenuProductCreateRequest(product2.getId(), 1)
                ));
        final Menu menu = new Menu(1L, "후라이드 양념 세트", new Price(BigDecimal.valueOf(30000)), menuGroup.getId());

        MenuProduct menuProduct1 = new MenuProduct(menu, product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(menu, product2.getId(), 1);
        menu.addMenuProduct(menuProduct1);
        menu.addMenuProduct(menuProduct2);

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.findById(menuProduct1.getProductId()))
                .willReturn(Optional.of(product1));
        given(productRepository.findById(menuProduct2.getProductId()))
                .willReturn(Optional.of(product2));

        given(menuRepository.save(any()))
                .willReturn(menu);

        // when & then
        assertThat(menuService.create(menuRequest)).isEqualTo(menu.getId());
        then(menuGroupRepository).should(times(1)).existsById(anyLong());
        then(productRepository).should(times(2)).findById(anyLong());
        then(menuRepository).should(times(1)).save(any());
        then(menuProductRepository).should(times(2)).save(any());
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenMenuGroupNotExist() {
        // given
        final MenuCreateRequest menuRequest = new MenuCreateRequest(
                "후라이드 양념 세트",
                BigDecimal.valueOf(30000),
                99L,
                List.of(
                        new MenuProductCreateRequest(10L, 1),
                        new MenuProductCreateRequest(11L, 1)
                ));
        final Menu menu = new Menu(1L, "후라이드 양념 세트", new Price(BigDecimal.valueOf(30000)), null);

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product product1 = new Product(100L, "후라이드 치킨", new Price(BigDecimal.valueOf(16000)));
        final Product product2 = new Product(101L, "양념 치킨", new Price(BigDecimal.valueOf(17000)));

        final MenuGroup menuGroup = new MenuGroup(10L, "치킨");
        final Menu menu1 = new Menu(1L, "후라이드 양념 세트", new Price(BigDecimal.valueOf(30000)), menuGroup.getId());
        final Menu menu2 = new Menu(2L, "후라이드 간장 세트", new Price(BigDecimal.valueOf(31000)), menuGroup.getId());

        MenuProduct menuProduct1 = new MenuProduct(menu1, product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(menu2, product2.getId(), 1);

        menu1.addMenuProduct(menuProduct1);
        menu2.addMenuProduct(menuProduct2);

        final List<Menu> menus = List.of(menu1, menu2);

        final List<MenuResponse> responses = menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());

        given(menuRepository.findAll())
                .willReturn(menus);

        // when & then
        assertThat(menuService.list()).usingRecursiveComparison().isEqualTo(responses);
    }
}
