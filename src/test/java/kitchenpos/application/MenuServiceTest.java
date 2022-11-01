package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
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

    @BeforeEach
    void setUp() {
        sut = new MenuService(menuRepository, menuGroupRepository, productRepository);
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
