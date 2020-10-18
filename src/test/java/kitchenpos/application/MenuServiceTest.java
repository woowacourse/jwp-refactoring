package kitchenpos.application;

import kitchenpos.config.IsolatedTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import kitchenpos.ui.dto.menu.MenuResponses;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends IsolatedTest {

    @Autowired
    private MenuService service;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("메뉴 생성 실패 - 가격이 음수일 때")
    @Test
    public void createFailPriceNegative() {
        menuGroupRepository.save(new MenuGroup(1L, "패스트 푸드"));
        MenuRequest request = new MenuRequest("포테이토 피자 세트", BigDecimal.valueOf(-1L), 1L, Lists.newArrayList());

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 존재 하지 않는 메뉴 그룹을 참조할 때")
    @Test
    public void createFailNotExistedMenuGroup() {
        MenuRequest request = new MenuRequest("포테이토 피자 세트", null, 10L, Lists.newArrayList());

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격이 products 가격 합보다 클 때")
    @Test
    public void createFailPriceOverSumOfProductsPrice() {
        initData();
        MenuRequest request = menuRequest_포테이토_피자_세트_priceOf(15_000L);

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    public void createMenu() {
        initData();
        MenuRequest request = menuRequest_포테이토_피자_세트_priceOf(13_000L);

        final MenuResponse response = service.create(request);

        assertThat(response.getMenuGroup().getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("포테이토 피자 세트");
        assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(13_000L));
        assertThat(response.getMenuProducts()).hasSize(2);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    public void readMenus() {
        initData();
        MenuRequest request = menuRequest_포테이토_피자_세트_priceOf(13_000L);
        final MenuResponse menu = service.create(request);

        final MenuResponses menus = service.list();

        assertThat(menus.getMenuResponses()).hasSize(1);
        assertThat(menus.getMenuResponses().get(0).getName()).isEqualTo("포테이토 피자 세트");
        assertThat(menus.getMenuResponses().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(13_000L));
        assertThat(menus.getMenuResponses().get(0).getMenuGroup().getName()).isEqualTo("패스트 푸드");
        assertThat(menus.getMenuResponses().get(0).getMenuProducts()).hasSize(2);
    }

    private void initData() {
        menuGroupRepository.save(new MenuGroup(1L, "패스트 푸드"));
        productRepository.save(new Product(1L, "포테이토 피자", BigDecimal.valueOf(12_000L)));
        productRepository.save(new Product(2L, "콜라", BigDecimal.valueOf(2_000L)));
    }

    private MenuRequest menuRequest_포테이토_피자_세트_priceOf(Long price) {
        return new MenuRequest(
                "포테이토 피자 세트",
                BigDecimal.valueOf(price),
                1L,
                Lists.newArrayList(
                        new MenuProductRequest(1L, null, 1L, 1L),
                        new MenuProductRequest(2L, null, 2L, 1L)
                )
        );
    }
}
