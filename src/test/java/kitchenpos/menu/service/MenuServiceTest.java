package kitchenpos.menu.service;

import static kitchenpos.fixtures.MenuFixtures.createMenu;
import static kitchenpos.fixtures.MenuFixtures.createMenuRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.fixtures.ServiceTest;
import kitchenpos.menu.service.dto.MenuRequest;
import kitchenpos.menu.service.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.MenuGroupFixtures;
import kitchenpos.fixtures.ProductFixtures;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuServiceTest extends ServiceTest {

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

    private Menu menu;
    private MenuRequest request;

    @BeforeEach
    void setUp() {
        menu = createMenu();
        request = createMenuRequest();
    }

    @Test
    void 메뉴를_생성한다() {
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(MenuGroupFixtures.createMenuGroup()));
        given(productRepository.findById(any())).willReturn(Optional.of(ProductFixtures.createProduct()));
        given(menuRepository.save(any())).willReturn(menu);
        given(menuProductRepository.saveAll(any())).willReturn(MenuFixtures.createMenuProducts());

        assertDoesNotThrow(() -> menuService.create(request));
        verify(menuProductRepository, times(1)).saveAll(any());
        verify(menuRepository, times(1)).save(any());
    }

    @Test
    void 메뉴_그룹이_존재하지_않을_경우_예외를_반환한다() {
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> menuService.create(request));
    }

    @Test
    void 메뉴_리스트를_반환한다() {
        given(menuRepository.findAll()).willReturn(Collections.singletonList(menu));

        List<MenuResponse> menuResponses = assertDoesNotThrow(() -> menuService.list());
        menuResponses.stream()
            .map(MenuResponse::getMenuProducts)
            .forEach(menuProducts -> assertThat(menuProducts).isNotEmpty());
    }
}
