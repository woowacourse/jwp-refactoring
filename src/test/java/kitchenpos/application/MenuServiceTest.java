package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.MenuRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    private static final long MENU_ID = 1L;
    private static final long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    private MenuService sut;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @BeforeEach
    void setUp() {
        sut = new MenuService(menuRepository, menuGroupRepository, productRepository, menuProductRepository);
    }

    @DisplayName("새로운 메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuProductRequest menuProduct = createMenuProductRequest();
        final MenuRequest request = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), 2L, List.of(menuProduct));

        // when
        final MenuResponse response = sut.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        final Menu foundMenu = menuRepository.findById(response.getId()).get();
        assertThat(foundMenu)
                .usingRecursiveComparison()
                .ignoringFields("id", "menuProducts")
                .isEqualTo(response);
    }

    @DisplayName("메뉴는 반드시 어느 메뉴 그룹에 속해있어야 한다.")
    @Test
    void createWithNonGroup() {
        // given
        final long notExistMenuGroupId = -1L;
        final MenuProductRequest menuProduct = createMenuProductRequest();
        final MenuRequest request = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), notExistMenuGroupId, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품(product)의 금액 총합(가격 * 수량) 보다 크면 안된다.")
    @Test
    void createWithLessPriceThenTotalProductPrice() {
        // given
        final MenuProductRequest menuProduct = createMenuProductRequest();
        final MenuRequest request = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16001), 2L, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품(Product)의 조회결과가 없는 경우 메뉴를 생성할 수 없다.")
    @Test
    void createMenuWithEmptyProduct() {
        // given
        final MenuRequest request = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), 2L, List.of());

        // when & then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 리스트를 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<MenuResponse> menus = sut.list();

        // then
        assertThat(menus)
                .hasSize(6)
                .extracting(MenuResponse::getName, menu -> menu.getPrice().longValue(), MenuResponse::getMenuGroupId)
                .containsExactlyInAnyOrder(
                        tuple("후라이드치킨", 16000L, 2L),
                        tuple("양념치킨", 16000L, 2L),
                        tuple("반반치킨", 16_000L, 2L),
                        tuple("통구이", 16_000L, 2L),
                        tuple("간장치킨", 17_000L, 2L),
                        tuple("순살치킨", 17_000L, 2L)
                );
    }

    private MenuProductRequest createMenuProductRequest() {
        return new MenuProductRequest(MENU_ID, PRODUCT_ID, QUANTITY);
    }
}
