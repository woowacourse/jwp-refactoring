package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.menu.application.MenuMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private EntityManager manager;

    @Test
    void 메뉴_그룹_ID_가_존재하지_않은_경우_예외가_발생한다() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "후라이드+후라이드",
                19000,
                0L,
                List.of(menuProductRequest)
        );

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 메뉴 그룹 ID가 존재하지 않습니다.");
    }

    @Test
    void 제품_ID가_존재하지_않는_경우_예외가_발생한다() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(0L, 2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "후라이드+후라이드",
                19000,
                1L,
                List.of(menuProductRequest)
        );

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 제품 ID가 존재하지 않습니다.");
    }

    @Test
    void 전체_제품의_총_가격_보다_메뉴가격이_크면_예외가_발생한다() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "후라이드+후라이드",
                32001,
                1L,
                List.of(menuProductRequest)
        );

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 단품을 가격보다 높을 수 없습니다.");
    }

    @Test
    void 메뉴_생성를_생성할_수_있다() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "후라이드+후라이드",
                19000,
                1L,
                List.of(menuProductRequest)
        );

        MenuResponse menu = menuService.create(menuCreateRequest);
        manager.flush();
        manager.clear();
        Menu saveMenu = menuRepository.findById(menu.getId()).orElseThrow();
        Assertions.assertThat(menu.getId()).isEqualTo(saveMenu.getId());
    }
}
