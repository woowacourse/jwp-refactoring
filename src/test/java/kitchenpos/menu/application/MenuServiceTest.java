package kitchenpos.menu.application;

import kitchenpos.MockServiceTest;
import kitchenpos.menu.application.dto.CreateMenuDto;
import kitchenpos.menu.application.dto.CreateMenuProductDto;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductQuantity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.MenuGroupException;
import kitchenpos.menu.exception.MenuPriceException;
import kitchenpos.product.exception.ProductException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class MenuServiceTest extends MockServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Long menuGroupId = 1L;
        Menu menu = new Menu(1L, "chicken + chicken + pizza", BigDecimal.valueOf(29000L), menuGroupId);

        Product chicken = new Product(1L, new ProductName("chicken"), new ProductPrice(BigDecimal.valueOf(10000L)));
        Product pizza = new Product(10L, new ProductName("pizza"), new ProductPrice(BigDecimal.valueOf(9500L)));
        MenuProduct twoChicken = new MenuProduct(1L, menu, chicken, new MenuProductQuantity(2L));
        MenuProduct onePizza = new MenuProduct(5L, menu, pizza, new MenuProductQuantity(1L));
        menu.addMenuProducts(List.of(twoChicken, onePizza));

        BDDMockito.given(menuRepository.findAllWithMenuProducts())
                .willReturn(List.of(menu));

        // when
        List<MenuDto> actual = menuService.list();

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actual.size()).isEqualTo(1);
        softAssertions.assertThat(actual.get(0).getId()).isEqualTo(menu.getId());
        softAssertions.assertThat(actual.get(0).getName()).isEqualTo(menu.getName());
        softAssertions.assertThat(actual.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        softAssertions.assertThat(actual.get(0).getPrice()).isEqualTo(menu.getPrice());
        softAssertions.assertThat(actual.get(0).getMenuProducts().size()).isEqualTo(2);
        softAssertions.assertAll();
    }

    @Test
    void 메뉴를_추가한다() {
        // given
        Long menuGroupId = 1L;
        Menu menu = new Menu(1L, "chicken + chicken + pizza", BigDecimal.valueOf(29000L), menuGroupId);

        Product chicken = new Product(1L, new ProductName("chicken"), new ProductPrice(BigDecimal.valueOf(10000L)));
        Product pizza = new Product(10L, new ProductName("pizza"), new ProductPrice(BigDecimal.valueOf(9500L)));
        MenuProduct twoChicken = new MenuProduct(1L, menu, chicken, new MenuProductQuantity(2L));
        MenuProduct onePizza = new MenuProduct(5L, menu, pizza, new MenuProductQuantity(1L));
        menu.addMenuProducts(List.of(twoChicken, onePizza));

        BDDMockito.given(menuGroupRepository.existsById(BDDMockito.anyLong()))
                .willReturn(true);
        BDDMockito.given(productRepository.findById(pizza.getId()))
                .willReturn(Optional.of(pizza));
        BDDMockito.given(productRepository.findById(chicken.getId()))
                .willReturn(Optional.of(chicken));
        BDDMockito.given(menuRepository.save(BDDMockito.any(Menu.class)))
                .willReturn(menu);

        CreateMenuDto createMenuDto = new CreateMenuDto(
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                List.of(
                        new CreateMenuProductDto(chicken.getId(), twoChicken.getQuantity()),
                        new CreateMenuProductDto(pizza.getId(), onePizza.getQuantity())
                ));

        // when
        MenuDto actual = menuService.create(createMenuDto);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actual.getId()).isEqualTo(menu.getId());
        softAssertions.assertThat(actual.getName()).isEqualTo(menu.getName());
        softAssertions.assertThat(actual.getPrice()).isEqualTo(menu.getPrice());
        softAssertions.assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        softAssertions.assertThat(actual.getMenuProducts().size()).isEqualTo(2);
        softAssertions.assertAll();
    }

    @Test
    void 메뉴를_추가할_때_메뉴가격이_null_이면_예외를_던진다() {
        // given
        Long menuGroupId = 1L;
        BDDMockito.given(menuGroupRepository.existsById(BDDMockito.anyLong()))
                .willReturn(true);

        BigDecimal price = null;
        CreateMenuDto createMenuDto = new CreateMenuDto(
                "chicken + chicken + pizza",
                price,
                menuGroupId,
                List.of(
                        new CreateMenuProductDto(1L, 2L),
                        new CreateMenuProductDto(2L, 1L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(createMenuDto))
                .isInstanceOf(MenuPriceException.class);
    }

    @Test
    void 메뉴를_추가할_때_메뉴가격이_음수이면_예외를_던진다() {
        // given
        Long menuGroupId = 1L;

        BDDMockito.given(menuGroupRepository.existsById(BDDMockito.anyLong()))
                .willReturn(true);

        BigDecimal price = BigDecimal.valueOf(-1000L);
        CreateMenuDto createMenuDto = new CreateMenuDto(
                "chicken + chicken + pizza",
                price,
                menuGroupId,
                List.of(
                        new CreateMenuProductDto(1L, 2L),
                        new CreateMenuProductDto(2L, 1L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(createMenuDto))
                .isInstanceOf(MenuPriceException.class);
    }

    @Test
    void 메뉴를_추가할_때_메뉴가격이_상품가격_곱하기_상품수량의_합보다_크면_예외를_던진다() {
        // given
        Long menuGroupId = 1L;

        Product chicken = new Product(1L, new ProductName("chicken"), new ProductPrice(BigDecimal.valueOf(10000L)));
        Product pizza = new Product(10L, new ProductName("pizza"), new ProductPrice(BigDecimal.valueOf(9500L)));

        BDDMockito.given(menuGroupRepository.existsById(BDDMockito.anyLong()))
                .willReturn(true);
        BDDMockito.given(productRepository.findById(chicken.getId()))
                .willReturn(Optional.of(chicken));
        BDDMockito.given(productRepository.findById(pizza.getId()))
                .willReturn(Optional.of(pizza));

        BigDecimal correctPrice = chicken.getPrice().multiply(BigDecimal.valueOf(2L))
                .add(pizza.getPrice().multiply(BigDecimal.ONE));
        BigDecimal greaterThanCorrectPrice = correctPrice.add(BigDecimal.TEN);

        CreateMenuDto createMenuDto = new CreateMenuDto(
                "two chicken + one pizza",
                greaterThanCorrectPrice,
                menuGroupId,
                List.of(
                        new CreateMenuProductDto(chicken.getId(), 2L),
                        new CreateMenuProductDto(pizza.getId(), 1L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(createMenuDto))
                .isInstanceOf(MenuException.class);
    }

    @Test
    void 메뉴를_추가할_때_메뉴_안의_상품이_존재하지_않으면_예외를_던진다() {
        // given
        Long menuGroupId = 1L;

        BDDMockito.given(menuGroupRepository.existsById(BDDMockito.anyLong()))
                .willReturn(true);
        BDDMockito.given(productRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.empty());

        BigDecimal price = BigDecimal.valueOf(1000L);
        CreateMenuDto createMenuDto = new CreateMenuDto(
                "chicken + chicken + pizza",
                price,
                menuGroupId,
                List.of(
                        new CreateMenuProductDto(1L, 2L),
                        new CreateMenuProductDto(10L, 1L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(createMenuDto))
                .isInstanceOf(ProductException.class);
    }

    @Test
    void 메뉴를_추가할_때_메뉴그룹이_존재하지_않으면_예외를_던진다() {
        // given
        Long menuGroupId = 1L;

        BDDMockito.given(menuGroupRepository.existsById(BDDMockito.anyLong()))
                .willReturn(false);

        BigDecimal price = BigDecimal.valueOf(1000L);
        CreateMenuDto createMenuDto = new CreateMenuDto(
                "chicken + chicken + pizza",
                price,
                menuGroupId,
                List.of(
                        new CreateMenuProductDto(1L, 2L),
                        new CreateMenuProductDto(10L, 1L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(createMenuDto))
                .isInstanceOf(MenuGroupException.class);
    }
}
