package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.menu.MenuProductRequest;
import kitchenpos.application.dto.request.menu.MenuRequest;
import kitchenpos.application.dto.response.menu.MenuResponse;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.InvalidMenuProductsPriceException;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private static List<MenuProductRequest> menuProductRequests;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup.MenuGroupBuilder()
                .setName("분식집")
                .build();

        menuGroupRepository.save(menuGroup);

        Product product1 = new Product.ProductBuilder()
                                    .setName("떡볶이")
                                    .setPrice(3500)
                                    .build();
        Product product2 = new Product.ProductBuilder()
                                    .setName("순대")
                                    .setPrice(4000)
                                    .build();
        Product product3 = new Product.ProductBuilder()
                                    .setName("튀김")
                                    .setPrice(4000)
                                    .build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L, 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L, 1L);
        MenuProductRequest menuProductRequest3 = new MenuProductRequest(3L, 1L);

        menuProductRequests = List.of(menuProductRequest1, menuProductRequest2, menuProductRequest3);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        MenuRequest menuRequest = new MenuRequest("떡순튀",
                11000,
                1L,
                menuProductRequests);

        //when
        MenuResponse actual = menuService.create(menuRequest);

        //then
        assertThat(actual.getName()).isEqualTo("떡순튀");
        assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(11000));
    }

    @DisplayName("메뉴 등록 실패 - 올바르지 않은 가격")
    @Test
    void createFailInvalidPrice() {
        //given
        MenuRequest menuRequest = new MenuRequest("떡순튀",
                -1000,
                1L,
                menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("메뉴 등록 실패 - 메뉴 그룹이 존재하지 않을 경우")
    @Test
    void createFailNotExistMenuGroup() {
        //given
        MenuRequest menuRequest = new MenuRequest("떡순튀",
                11000,
                2L,
                menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
        .isInstanceOf(NotFoundMenuGroupException.class);
    }

    @DisplayName("메뉴 등록 실패 - 상품이 존재하지 않을 경우")
    @Test
    void createFailNotExistProduct() {
        //given
        MenuProductRequest notExistMenuProductRequest = new MenuProductRequest(4L, 1L);

        MenuRequest menuRequest = new MenuRequest("떡순튀",
                1000,
                1L,
                List.of(notExistMenuProductRequest));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundProductException.class);
    }

    @DisplayName("메뉴 등록 실패 - 단일 상품가격의 합보다 메뉴의 가격이 클 경우")
    @Test
    void createFailInvalidMenuPrice() {
        //given
        MenuRequest menuRequest = new MenuRequest("떡순튀",
                20000,
                1L,
                menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidMenuProductsPriceException.class);
    }

    @DisplayName("메뉴 목록을 불러온다.")
    @Test
    void list() {
        //given
        MenuRequest menuRequest1 = new MenuRequest("떡순튀1",
                11000,
                1L,
                menuProductRequests);

        MenuRequest menuRequest2 = new MenuRequest("떡순튀2",
                11000,
                1L,
                menuProductRequests);

        menuService.create(menuRequest1);
        menuService.create(menuRequest2);

        //when
        List<MenuResponse> actual = menuService.list();

        //then
        assertThat(actual.get(0).getName()).isEqualTo("떡순튀1");
        assertThat(actual.get(1).getName()).isEqualTo("떡순튀2");
        assertThat(actual).hasSize(2);
    }
}