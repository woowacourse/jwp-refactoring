package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
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

import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;


    @Test
    @DisplayName("메뉴를 등록한다")
    void create() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2l);

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
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2l);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 가격이 0보다 작으면 예외가 발생한다.")
    void create_invalidPrice_negative() {
        // given
        final BigDecimal invalidPrice = BigDecimal.valueOf(-1);

        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2l);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 지정한 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    void create_nullMenuGroup() {
        // given
        final Long invalidMenuGroupId = -999L;

        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2l);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", BigDecimal.valueOf(30000), invalidMenuGroupId, List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 등록할 때 상품의 가격 합이 메뉴의 가격보다 작으면 예외가 발생한다.")
    void create_invalidPrice_moreThanSum() {
        // given
        final BigDecimal invalidPrice = BigDecimal.valueOf(50000);
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final CreateMenuProductRequest 후라이드_2개 = new CreateMenuProductRequest(후라이드.getId(), 2l);

        final CreateMenuRequest invalidMenu = new CreateMenuRequest("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다")
    void list() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final Product 양념치킨 = productRepository.save(new Product("양념치킨", BigDecimal.valueOf(20000)));

        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = menuProduct(후라이드.getId(), 2l);
        final MenuProduct 후라이드_1개 = menuProduct(후라이드.getId(), 1l);
        final MenuProduct 양념치킨_1개 = menuProduct(양념치킨.getId(), 1l);

        final Menu 후라이드_후라이드 = menuRepository.save(menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개)));
        final Menu 후라이드_양념치킨 = menuRepository.save(menu("후라이드+양념치킨", BigDecimal.valueOf(33000), 두마리메뉴.getId(), List.of(후라이드_1개, 양념치킨_1개)));

        menuProductRepository.save(menuProduct(후라이드_후라이드.getId(), 후라이드_2개));
        menuProductRepository.save(menuProduct(후라이드_양념치킨.getId(), 후라이드_1개));
        menuProductRepository.save(menuProduct(후라이드_양념치킨.getId(), 양념치킨_1개));

        final Menu expect1 = 후라이드_후라이드;
        final Menu expect2 = 후라이드_양념치킨;

        // when
        final List<Menu> actual = menuService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo(expect1.getName());
            softAssertions.assertThat(actual.get(1).getName()).isEqualTo(expect2.getName());
        });
    }
}
