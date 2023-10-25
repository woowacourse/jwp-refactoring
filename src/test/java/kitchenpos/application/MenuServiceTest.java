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
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.exception.MenuGroupException;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.common.exception.PriceException;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.exception.ProductException;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.application.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

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
    private MenuProduct 메뉴_상품;

    @BeforeEach
    void setUp() {
        this.메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹(null, "메뉴 그룹"));
        this.상품 = productRepository.save(새로운_상품(null, "상품", new BigDecimal(10000)));
        this.메뉴_상품 = 새로운_메뉴_상품(null, 상품, 3);
    }

    @Test
    void 등록된_상품들을_메뉴로_등록한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        MenuResponse 메뉴_생성_응답 = menuService.create(메뉴_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(메뉴_생성_응답.getId()).isNotNull();
            softly.assertThat(메뉴_생성_응답.getName()).isEqualTo("메뉴");
            softly.assertThat(메뉴_생성_응답.getPrice()).isEqualByComparingTo("30000.00");
            softly.assertThat(메뉴_생성_응답.getMenuGroupResponse().getId()).isEqualTo(메뉴_그룹.getId());
            softly.assertThat(메뉴_생성_응답.getMenuProductResponses().get(0).getProductId()).isEqualTo(상품.getId());
            softly.assertThat(메뉴_생성_응답.getMenuProductResponses().get(0).getQuantity()).isEqualTo(3);
        });
    }

    @Test
    void 메뉴의_이름은_최대_255자이다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("짱".repeat(256), new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴의 이름이 유효하지 않습니다.");
    }

    @Test
    void 메뉴_가격이_0원_이상이어야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal(-1), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 메뉴_가격이_100조원_미만이어야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(Math.pow(10, 20)), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 메뉴에_속한_상품_금액의_합은_메뉴의_가격보다_크거나_같아야_한다() {
        Product 새상품 = productRepository.save(새로운_상품(null, "새상품", new BigDecimal(40000)));
        MenuProductCreateRequest 메뉴_상품_생성_요청1 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuProductCreateRequest 메뉴_상품_생성_요청2 = new MenuProductCreateRequest(새상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("300000000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청1, 메뉴_상품_생성_요청2));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴 상품의 가격의 총합이 메뉴의 가격보다 작습니다.");
    }

    @Test
    void 메뉴는_존재하는_메뉴_그룹에_속해야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), Long.MIN_VALUE, List.of(메뉴_상품_생성_요청));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(MenuGroupException.class)
                .hasMessage("해당하는 메뉴 그룹이 없습니다.");
    }

    @Test
    void 존재하지_않는_상품이_등록될_수_없다() {
        MenuProductCreateRequest 존재하지_않는_메뉴_상품_생성_요청 = new MenuProductCreateRequest(Long.MIN_VALUE, 1);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(존재하지_않는_메뉴_상품_생성_요청));

        assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(ProductException.class)
                .hasMessage("해당하는 상품이 없습니다.");
    }

    @Test
    void 메뉴의_목록을_조회한다() {
        Menu 메뉴1 = menuRepository.save(새로운_메뉴("메뉴1", new BigDecimal("30000.00"), 메뉴_그룹));
        Menu 메뉴2 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal("30000.00"), 메뉴_그룹));

        메뉴1.addMenuProducts(List.of(메뉴_상품));
        메뉴2.addMenuProducts(List.of(메뉴_상품));
        List<MenuResponse> 메뉴_목록_조회_응답 = menuService.readAll();

        assertThat(메뉴_목록_조회_응답).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(MenuResponse.from(메뉴1), MenuResponse.from(메뉴2)));
    }
}
