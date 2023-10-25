package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.ui.dto.CreateMenuProductRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;


    @Test
    @DisplayName("메뉴를 등록한다")
    void create() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2L);

        final CreateMenuRequest menu = new CreateMenuRequest("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개));

        // when
        final Menu actual = menuService.create(menu);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @DisplayName("메뉴를 등록할 때 가격이 없으면 예외가 발생한다.")
    void create_invalidPrice_null() {
        // given
        final BigDecimal invalidPrice = null;

        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2L);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴를 등록할 때 가격이 0보다 작으면 예외가 발생한다.")
    void create_invalidPrice_negative() {
        // given
        final BigDecimal invalidPrice = BigDecimal.valueOf(-1);

        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2L);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴를 등록할 때 지정한 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    void create_nullMenuGroup() {
        // given
        final Long invalidMenuGroupId = -999L;

        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2L);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", BigDecimal.valueOf(30000), invalidMenuGroupId, List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("메뉴를 등록할 때 상품이 유효한 상품이 아니라면 예외가 발생한다.")
    void create_invalidProduct() {
        // given
        final Long invalidProductId = -999L;
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        final CreateMenuProductRequest invalidMenuProductRequest = new CreateMenuProductRequest(invalidProductId, 2L);
        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(invalidMenuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("메뉴를 등록할 때 상품의 가격 합이 메뉴의 가격보다 작으면 예외가 발생한다.")
    void create_invalidPrice_moreThanSum() {
        // given
        final BigDecimal invalidPrice = BigDecimal.valueOf(50000);
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2L);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 메뉴를 구성하는 상품의 가격 합보다 작아야 합니다.");
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다")
    void list() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final Product 양념치킨 = productRepository.save(new Product("양념치킨", BigDecimal.valueOf(20000)));

        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드, 2L);
        final MenuProduct 후라이드_1개 = new MenuProduct(후라이드, 1L);
        final MenuProduct 양념치킨_1개 = new MenuProduct(양념치킨, 1L);

        final Menu 후라이드_후라이드 = menuRepository.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴));

        후라이드_후라이드.addMenuProducts(new MenuProducts(List.of(후라이드_2개)));
        final Menu 후라이드_양념치킨 = menuRepository.save(new Menu("후라이드+양념치킨", BigDecimal.valueOf(33000), 두마리메뉴));
        후라이드_양념치킨.addMenuProducts(new MenuProducts(List.of(후라이드_1개, 양념치킨_1개)));

        // when
        final List<Menu> actual = menuService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(후라이드_후라이드);
            softAssertions.assertThat(actual.get(1)).isEqualTo(후라이드_양념치킨);
        });
    }
}
