package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_상품;
import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup 메뉴_그룹;
    private Product 상품;
    private MenuProduct 메뉴_상품;

    @BeforeEach
    void setUp() {
        this.메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        this.상품 = productRepository.save(새로운_상품("상품", new BigDecimal(10000)));
        this.메뉴_상품 = 새로운_메뉴_상품(null, 상품.getId(), 3);
    }

    @Test
    void 등록된_상품들을_메뉴로_등록한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품));

        Menu 등록된_메뉴 = menuService.create(메뉴);

        assertSoftly(softly -> {
            softly.assertThat(등록된_메뉴.getId()).isNotNull();
            softly.assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(메뉴.getPrice());
            softly.assertThat(등록된_메뉴).usingRecursiveComparison()
                    .ignoringFields("id", "menuProducts")
                    .isEqualTo(메뉴);
            softly.assertThat(등록된_메뉴.getMenuProducts()).hasSize(1)
                    .usingRecursiveFieldByFieldElementComparatorIgnoringFields("seq")
                    .containsOnly(메뉴_상품);
        });
    }

    @Test
    void 메뉴의_이름은_최대_255자이다() {
        Menu 메뉴 = 새로운_메뉴("짱".repeat(256), new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0원_이상이어야_한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", new BigDecimal(-1), 메뉴_그룹.getId(), List.of(메뉴_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_100조원_미만이어야_한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", BigDecimal.valueOf(Math.pow(10, 20)), 메뉴_그룹.getId(), List.of(메뉴_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴에_속한_상품_금액의_합은_메뉴의_가격보다_크거나_같아야_한다() {
        Product 새상품 = 새로운_상품("새상품", new BigDecimal(40000));
        MenuProduct 새메뉴_상품 = 새로운_메뉴_상품(null, 새상품.getId(), 1);
        Menu 메뉴 = 새로운_메뉴("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품, 새메뉴_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴는_특정_메뉴_그룹에_속해야_한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", new BigDecimal("30000.00"), null, List.of(메뉴_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴는_존재하는_메뉴_그룹에_속해야_한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", new BigDecimal("30000.00"), Long.MIN_VALUE, List.of(메뉴_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이_등록될_수_없다() {
        MenuProduct 존재하지_않는_상품 = 새로운_메뉴_상품(null, Long.MIN_VALUE, 1);
        Menu 메뉴 = 새로운_메뉴("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(존재하지_않는_상품));

        assertThatThrownBy(() -> menuService.create(메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_목록을_조회한다() {
        Menu 메뉴1 = menuService.create(새로운_메뉴("메뉴1", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품)));
        Menu 메뉴2 = menuService.create(새로운_메뉴("메뉴2", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품)));

        List<Menu> 메뉴_목록 = menuService.list();

        assertThat(메뉴_목록).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("price", "menuProducts")
                .containsExactly(메뉴1, 메뉴2);
    }
}
