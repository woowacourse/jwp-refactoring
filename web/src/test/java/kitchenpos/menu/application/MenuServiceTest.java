package kitchenpos.menu.application;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_상품;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.MenuException;
import kitchenpos.exception.MenuGroupException;
import kitchenpos.exception.PriceException;
import kitchenpos.exception.ProductException;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
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

    @BeforeEach
    void setUp() {
        this.메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹(null, "메뉴 그룹"));
        this.상품 = productRepository.save(새로운_상품(null, "상품", new BigDecimal(10000)));
    }

    @Test
    void 등록된_상품들을_메뉴로_등록한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        MenuResponse 메뉴_생성_응답 = menuService.create(메뉴_생성_요청);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(메뉴_생성_응답.getId()).isNotNull();
            softly.assertThat(메뉴_생성_응답.getName()).isEqualTo("메뉴");
            softly.assertThat(메뉴_생성_응답.getPrice()).isEqualByComparingTo("30000.00");
            softly.assertThat(메뉴_생성_응답.getMenuGroupResponse().getId()).isEqualTo(메뉴_그룹.getId());
            softly.assertThat(메뉴_생성_응답.getMenuProductResponses().get(0).getQuantity()).isEqualTo(3);
        });
    }

    @Test
    void 메뉴의_이름은_최대_255자이다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("짱".repeat(256), new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴의 이름이 유효하지 않습니다.");
    }

    @Test
    void 메뉴_가격이_0원_이상이어야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal(-1), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 메뉴_가격이_100조원_미만이어야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(Math.pow(10, 20)), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 메뉴에_속한_상품_금액의_합은_메뉴의_가격보다_크거나_같아야_한다() {
        Product 새상품 = productRepository.save(새로운_상품(null, "새상품", new BigDecimal(40000)));
        MenuProductCreateRequest 메뉴_상품_생성_요청1 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuProductCreateRequest 메뉴_상품_생성_요청2 = new MenuProductCreateRequest(새상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("300000000.00"), 메뉴_그룹.getId(), List.of(메뉴_상품_생성_요청1, 메뉴_상품_생성_요청2));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴 상품의 가격의 총합이 메뉴의 가격보다 작습니다.");
    }

    @Test
    void 메뉴는_존재하는_메뉴_그룹에_속해야_한다() {
        MenuProductCreateRequest 메뉴_상품_생성_요청 = new MenuProductCreateRequest(상품.getId(), 3);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), Long.MIN_VALUE, List.of(메뉴_상품_생성_요청));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(MenuGroupException.class)
                .hasMessage("해당하는 메뉴 그룹이 없습니다.");
    }

    @Test
    void 존재하지_않는_상품이_등록될_수_없다() {
        MenuProductCreateRequest 존재하지_않는_메뉴_상품_생성_요청 = new MenuProductCreateRequest(Long.MIN_VALUE, 1);
        MenuCreateRequest 메뉴_생성_요청 = new MenuCreateRequest("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), List.of(존재하지_않는_메뉴_상품_생성_요청));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴_생성_요청))
                .isInstanceOf(ProductException.class)
                .hasMessage("해당하는 상품이 없습니다.");
    }

    @Test
    void 메뉴의_목록을_조회한다() {
        Menu 메뉴1 = menuRepository.save(새로운_메뉴("메뉴1", new BigDecimal("30000.00"), 메뉴_그룹));
        Menu 메뉴2 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal("30000.00"), 메뉴_그룹));


        List<MenuResponse> 메뉴_목록_조회_응답 = menuService.readAll();

        Assertions.assertThat(메뉴_목록_조회_응답).hasSize(2);
    }
}
