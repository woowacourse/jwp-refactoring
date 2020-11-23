package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponses;

class MenuServiceTest extends TruncateDatabaseConfig {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @DisplayName("메뉴 생성 실패 - 가격 null")
    @ParameterizedTest
    @NullSource
    void createFail_When_Price_Null(BigDecimal price) {
        menuGroupRepository.save(new MenuGroup("치킨류"));
        MenuRequest menuRequest = new MenuRequest("치킨 반반 세트", price, 1L, Lists.newArrayList());

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격 음수")
    @Test
    void createFail_When_Negative_Price() {
        menuGroupRepository.save(new MenuGroup("치킨류"));
        MenuRequest menuRequest = new MenuRequest("치킨 반반 세트", BigDecimal.valueOf(-10L), 1L, Lists.newArrayList());

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 존재하지 않는 MenuGroupId")
    @Test
    void createFail_When_MenuGroupId_NotExist() {
        MenuRequest menuRequest = new MenuRequest("치킨 반반 세트", BigDecimal.valueOf(10_000L), 1L, Lists.newArrayList());
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 메뉴 가격이 Products 가격 합보다 클 경우")
    @Test
    void createFail_When_Price_Over_Sum_Of_ProductsPrice() {
        setTestData();
        MenuRequest menuRequest = new MenuRequest("치킨 반반 세트", BigDecimal.valueOf(30_000L), 1L, Lists.newArrayList(
            new MenuProductRequest(1L, 1L, 1L),
            new MenuProductRequest(1L, 2L, 1L)
        ));
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 리스트 조회")
    @Test
    void list() {
        setTestData();
        MenuRequest menuRequest = new MenuRequest("치킨 반반 세트", BigDecimal.valueOf(20_000L), 1L, Lists.newArrayList(
            new MenuProductRequest(1L, 1L, 1L),
            new MenuProductRequest(1L, 2L, 1L)
        ));
        menuService.create(menuRequest);

        MenuResponses menuResponses = menuService.list();

        assertAll(
            () -> assertThat(menuResponses.getMenuResponses().size()).isEqualTo(1)
        );
    }

    private void setTestData() {
        menuGroupRepository.save(new MenuGroup("치킨류"));
        productRepository.save(new Product("후라이드 치킨", BigDecimal.valueOf(10_000)));
        productRepository.save(new Product("양념 치킨", BigDecimal.valueOf(10_000)));
    }
}
