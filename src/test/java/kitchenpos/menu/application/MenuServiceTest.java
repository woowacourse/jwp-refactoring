package kitchenpos.menu.application;

import static kitchenpos.common.fixture.MenuFixtures.*;
import static kitchenpos.common.fixture.MenuGroupFixtures.*;
import static kitchenpos.common.fixture.MenuProductFixtures.*;
import static kitchenpos.common.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuService menuService;

    @Autowired
    public MenuServiceTest(final MenuRepository menuRepository,
                           final MenuGroupRepository menuGroupRepository,
                           final ProductRepository productRepository,
                           final MenuService menuService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuService = menuService;
    }

    @Test
    void menu를_생성한다() {
        MenuGroup 한마리메뉴 = menuGroupRepository.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productRepository.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        MenuSaveRequest request = generateMenuSaveRequest(
                "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts);

        MenuResponse actual = menuService.create(request);

        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo(request.getName());
            assertThat(actual.getPrice().compareTo(request.getPrice())).isEqualTo(0);
            assertThat(actual.getMenuGroupId()).isEqualTo(한마리메뉴.getId());
            assertThat(actual.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    void menu를_등록할_때_price가_null인_경우_예외를_던진다() {
        MenuSaveRequest request = generateMenuSaveRequest("후라이드치킨", null, 1L, List.of());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "price가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void menu를_등록할_때_price가_0미만인_경우_예외를_던진다(final int price) {
        MenuSaveRequest request =
                generateMenuSaveRequest("후라이드치킨", BigDecimal.valueOf(price), 1L, List.of());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu를_등록할_때_존재하지_않는_menuGroupId인_경우_예외를_던진다() {
        MenuSaveRequest request =
                generateMenuSaveRequest("후라이드치킨", BigDecimal.valueOf(16000), 0L, List.of());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu를_등록할_때_price가_menu에_속한_product의_총_price보다_큰_경우_예외를_던진다() {
        MenuGroup 한마리메뉴 = menuGroupRepository.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productRepository.save(generateProduct("후라이드", BigDecimal.valueOf(16000)));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        MenuSaveRequest request =
                generateMenuSaveRequest("후라이드치킨", BigDecimal.valueOf(17000), 한마리메뉴.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_list를_조회한다() {
        MenuGroup 한마리메뉴 = menuGroupRepository.save(generateMenuGroup("한마리메뉴"));
        menuRepository.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId()));
        menuRepository.save(generateMenu("양념치킨", BigDecimal.valueOf(17000), 한마리메뉴.getId()));

        List<MenuResponse> actual = menuService.list();

        assertThat(actual).hasSize(2);
    }
}
