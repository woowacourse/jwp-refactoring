package kitchenpos.acceptance.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroup savedMenuGroup;
    private MenuGroup savedMenuGroup2;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        MenuGroup recommendation = new MenuGroup("추천메뉴");
        savedMenuGroup = menuGroupRepository.save(recommendation);
        MenuGroup best = new MenuGroup("최고메뉴");
        savedMenuGroup2 = menuGroupRepository.save(best);

        Product chicken = new Product("강정치킨", BigDecimal.valueOf(17000));
        Product savedChicken = productRepository.save(chicken);

        menuProductRequest = new MenuProductRequest(savedChicken.getId(), 2);
    }

    @DisplayName("메뉴 등록 성공")
    @Test
    void create() {
        MenuRequest halfHalf = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest)
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menus",
                halfHalf,
                MenuResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MenuResponse response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo("후라이드+후라이드");
        assertThat(response.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(response.getMenuProductResponses()).hasSize(1);
    }

    @DisplayName("메뉴 등록 실패 - 가격 부재")
    @Test
    void createByNullPrice() {
        MenuRequest halfHalf = new MenuRequest(
                "후라이드+후라이드",
                null,
                savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest)
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menus",
                halfHalf,
                MenuResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 등록 실패 - 가격 0 미만")
    @Test
    void createByNegativePrice() {
        MenuRequest halfHalf = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(-1),
                savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest)
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menus",
                halfHalf,
                MenuResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 등록 실패 - 잘못된 그룹 메뉴 아이디")
    @Test
    void createByIncorrectMenuGroupId() {
        MenuRequest halfHalf = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                100L,
                Arrays.asList(menuProductRequest)
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menus",
                halfHalf,
                MenuResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("상품 등록 실패 - 잘못된 상품 아이디")
    @Test
    void createByIncorrectProductId() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(100L, 2);
        MenuRequest halfHalf = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest)
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menus",
                halfHalf,
                MenuResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("메뉴 등록 실패 - 메뉴 가격이 메뉴 상품의 금액 합산보다 큰 경우")
    @Test
    void createByIncorrectPrice() {
        MenuRequest halfHalf = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(35000),
                savedMenuGroup.getId(),
                Arrays.asList(menuProductRequest)
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menus",
                halfHalf,
                MenuResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        Menu halfHalf = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup);
        Menu halfHalf2 = new Menu("앙념+후라이드", BigDecimal.valueOf(19000), savedMenuGroup2);

        menuRepository.save(halfHalf);
        menuRepository.save(halfHalf2);

        ResponseEntity<List<MenuResponse>> responseEntity = testRestTemplate.exchange(
                "/api/menus",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuResponse>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<MenuResponse> response = responseEntity.getBody();
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting(MenuResponse::getName)
                .containsExactlyInAnyOrder("후라이드+후라이드", "앙념+후라이드");
    }
}
