package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductCreateInfo;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;

@ServiceTest
class MenuServiceTest {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        Product savedProduct1 = productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000L)));
        Product savedProduct2 = productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(20_000L)));
        Product savedProduct3 = productRepository.save(new Product("시원한 아이스 아메리카노", BigDecimal.valueOf(5_000L)));

        MenuProductCreateInfo menuProductCreateInfo1 = new MenuProductCreateInfo(savedProduct1.getId(), 2L);
        MenuProductCreateInfo menuProductCreateInfo2 = new MenuProductCreateInfo(savedProduct2.getId(), 1L);
        MenuProductCreateInfo menuProductCreateInfo3 = new MenuProductCreateInfo(savedProduct3.getId(), 1L);

        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test_group"));

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", BigDecimal.valueOf(45_000L),
            savedMenuGroup.getId(),
            Arrays.asList(menuProductCreateInfo1, menuProductCreateInfo2, menuProductCreateInfo3));
        MenuResponse actual = menuService.create(menuCreateRequest);

        assertAll(
            () -> assertThat(actual).extracting(MenuResponse::getId).isNotNull(),
            () -> assertThat(actual).extracting(MenuResponse::getName).isEqualTo(menuCreateRequest.getName()),
            () -> assertThat(actual).extracting(MenuResponse::getPrice, BIG_DECIMAL)
                .isEqualByComparingTo(menuCreateRequest.getPrice())
        );
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void list() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("test"));

        Product savedProduct1 = productRepository.save(new Product("test1", BigDecimal.valueOf(1_000L)));
        Product savedProduct2 = productRepository.save(new Product("test2", BigDecimal.valueOf(2_000L)));

        MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), 2L);
        MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), 2L);

        Menu menu1 = new Menu("test1", BigDecimal.valueOf(2_000L), savedMenuGroup.getId(),
            Collections.singletonList(menuProduct1));
        Menu menu2 = new Menu("test2", BigDecimal.valueOf(4_000L), savedMenuGroup.getId(),
            Collections.singletonList(menuProduct2));

        menuRepository.save(menu1);
        menuRepository.save(menu2);

        List<MenuResponse> actual = menuService.list();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual).element(0).extracting(MenuResponse::getMenuProductResponses, LIST).isNotEmpty(),
            () -> assertThat(actual).element(1).extracting(MenuResponse::getMenuProductResponses, LIST).isNotEmpty()
        );
    }
}