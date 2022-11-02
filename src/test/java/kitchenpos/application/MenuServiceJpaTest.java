package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.entity.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.jpa.dto.menu.MenuCreateRequest;
import kitchenpos.ui.jpa.dto.menu.MenuCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceJpaTest extends ServiceTestJpa {

    @Autowired
    private MenuServiceJpa menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("name");
        menuGroupRepository.save(menuGroup);

        Product product = new Product("name", 1000L);
        productRepository.save(product);
        menuProduct = new MenuProduct(product, 5);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("이름", 5000L, menuGroup.getId(), List.of(menuProduct));

        MenuCreateResponse menuCreateResponse = menuService.create(menuCreateRequest);

        assertThat(menuCreateResponse.getId()).isNotNull();
    }


    @DisplayName("메뉴를 모두 조회한다.")
    @Test
    void list() {
        int numberOfMenuBeforeCreate = menuService.list().size();
        menuService.create(new MenuCreateRequest("이름", 5000L, menuGroup.getId(), List.of(menuProduct)));

        int numberOfMenu = menuService.list().size();

        assertThat(numberOfMenuBeforeCreate + 1).isEqualTo(numberOfMenu);
    }
}
