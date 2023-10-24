package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.TestFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup 메뉴_그룹;
    private Product 상품;

    @BeforeEach
    void setUp() {
        this.메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        this.상품 = productRepository.save(새로운_상품(null, "상품", new BigDecimal(10000)));
    }

    @Test
    void 등록된_상품들을_메뉴로_등록한다() {
        final MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3L);
        final MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        final Menu menu = menuService.create(메뉴_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(menu.getId()).isNotNull();
            softly.assertThat(menu.getName().getName()).isEqualTo("메뉴");
        });
    }

    @Test
    void 메뉴_가격이_0원_이상이어야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3L);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal(-1), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액은 0 이상이어야 합니다.");
    }

    @Test
    void 메뉴의_목록을_조회한다() {
        final MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3L);
        final MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        menuService.create(메뉴_생성_요청);

        final List<Menu> menus = menuService.findAll();

        assertThat(menus).hasSize(1);
    }
}
