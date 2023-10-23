package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MenuServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.from("그룹1"));
        productRepository.save(Product.of("상품1", new BigDecimal("10.0")));
        productRepository.save(Product.of("상품2", new BigDecimal("20.0")));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // Given
        MenuCreateRequest createRequest = new MenuCreateRequest(
                "메뉴1",
                new BigDecimal("15.0"),
                menuGroupRepository.findAll().get(0).getId(),
                createMenuProductRequests()
        );

        // When
        Long createdMenuId = menuService.create(createRequest);

        // Then
        assertThat(createdMenuId).isNotNull();
        assertThat(menuRepository.findById(createdMenuId).get().getName()).isEqualTo("메뉴1");
        assertThat(menuRepository.findById(createdMenuId).get().getPrice()).isEqualTo(new BigDecimal("15.0"));
    }

    @Test
    @DisplayName("없는 그룹에 메뉴를 생성할 때 예외")
    void createWithInvalidMenuGroup() {
        // Given
        MenuCreateRequest createRequest = new MenuCreateRequest(
                "메뉴1",
                new BigDecimal("15.0"),
                999L,
                createMenuProductRequests()
        );

        // When & Then
        assertThatThrownBy(() -> menuService.create(createRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는 상품을 메뉴화 할 때 예외")
    void createWithInvalidProducts() {
        // Given
        MenuCreateRequest createRequest = new MenuCreateRequest(
                "메뉴1",
                new BigDecimal("15.0"),
                menuGroupRepository.findAll().get(0).getId(),
                createInvalidMenuProductRequests()
        );

        // When & Then
        assertThatThrownBy(() -> menuService.create(createRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 조회한다.")
    void findAll() {
        // Given
        MenuCreateRequest createRequest = new MenuCreateRequest(
                "메뉴1",
                new BigDecimal("15.0"),
                menuGroupRepository.findAll().get(0).getId(),
                createMenuProductRequests()
        );
        menuService.create(createRequest);

        // When
        List<MenuResponse> menus = menuService.findAll();

        // Then
        MenuResponse menuResponse = menus.get(6);
        assertThat(menus).hasSize(7);
        assertThat(menuResponse.getName()).isEqualTo("메뉴1");
        assertThat(menuResponse.getPrice()).isEqualTo(new BigDecimal("15.0"));
    }

    private List<MenuProductRequest> createMenuProductRequests() {
        List<MenuProductRequest> requests = new ArrayList<>();
        requests.add(new MenuProductRequest(productRepository.findAll().get(0).getId(), 2));
        requests.add(new MenuProductRequest(productRepository.findAll().get(1).getId(), 3));
        return requests;
    }

    private List<MenuProductRequest> createInvalidMenuProductRequests() {
        List<MenuProductRequest> requests = new ArrayList<>();
        requests.add(new MenuProductRequest(999L, 2));
        return requests;
    }
}
