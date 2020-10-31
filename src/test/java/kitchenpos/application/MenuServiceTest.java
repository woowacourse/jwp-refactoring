package kitchenpos.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.model.MenuGroupRepository;
import kitchenpos.factory.MenuFactory;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.ProductFactory;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    private MenuProductFactory menuProductFactory = new MenuProductFactory();
    private MenuFactory menuFactory = new MenuFactory();
    private ProductFactory productFactory = new ProductFactory();

    @DisplayName("메뉴 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("메뉴를 생성한다.", this::createSuccess),
                dynamicTest("메뉴의 가격이 존재해야 한다.", this::noPriceMenu),
                dynamicTest("가격은 음수일 수 없다.", this::negativePriceMenu),
                dynamicTest("메뉴 상품이 존재해야 한다.", this::noMenuGroup),
                dynamicTest("상품이 존재해야 한다.", this::noProduct),
                dynamicTest("메뉴의 가격은 상품의 가격의 총 합보다 작아야 한다.", this::bigMenuPrice)
        );
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void list() {
        MenuProduct savedMenuProduct = menuProductFactory.create(1L, 1L, 2);
        Menu savedMenu = menuFactory.create(1L, "후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(savedMenuProduct));

        given(menuDao.findAll()).willReturn(singletonList(savedMenu));
        given(menuProductDao.findAllByMenuId(savedMenu.getId()))
                .willReturn(singletonList(savedMenuProduct));

        List<Menu> list = menuService.list();

        assertAll(
                () -> assertThat(list.get(0).getId()).isEqualTo(savedMenu.getId()),
                () -> assertThat(list.get(0).getName()).isEqualTo(savedMenu.getName()),
                () -> assertThat(list.get(0).getPrice()).isEqualTo(savedMenu.getPrice()),
                () -> assertThat(list.get(0).getMenuGroupId()).isEqualTo(
                        savedMenu.getMenuGroupId()),
                () -> assertThat(list.get(0).getMenuProducts().get(0)).isEqualTo(
                        savedMenu.getMenuProducts().get(0))
        );
    }

    private void createSuccess() {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(menuProduct));
        Product product = productFactory.create(1L, BigDecimal.valueOf(17_000L));
        Menu savedMenu = menuFactory.create(1L, "후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(menuProduct));
        MenuProduct savedMenuProduct = menuProductFactory.create(1L, 1L, 2);

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
        given(menuDao.save(menu)).willReturn(savedMenu);
        given(menuProductDao.save(menuProduct)).willReturn(savedMenuProduct);

        Menu actual = menuService.create(menu);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(savedMenu.getId()),
                () -> assertThat(actual.getName()).isEqualTo(savedMenu.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(savedMenu.getPrice()),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId()),
                () -> assertThat(actual.getMenuProducts()).isEqualTo(savedMenu.getMenuProducts())
        );
    }

    private void noPriceMenu() {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", null, 1L,
                singletonList(menuProduct));

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private void negativePriceMenu() {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", BigDecimal.valueOf(-1L), 1L,
                singletonList(menuProduct));

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private void noMenuGroup() {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(menuProduct));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private void noProduct() {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(menuProduct));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private void bigMenuPrice() {
        MenuProduct menuProduct = menuProductFactory.create(1L, 2);
        Menu menu = menuFactory.create("후라이드+후라이드", BigDecimal.valueOf(17_000L), 1L,
                singletonList(menuProduct));
        Product product = productFactory.create(1L, BigDecimal.valueOf(8_400L));

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }
}