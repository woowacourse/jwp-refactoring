package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menuproduct.MenuProductRepository;
import kitchenpos.menuproduct.request.MenuProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
class MenuServiceSpringBootTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private MenuService service;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        //given
        final List<MenuProductRequest> menuProducts = List.of(
                new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 3)
        );
        final MenuRequest menuRequest = new MenuRequest(
                "menuName",
                BigDecimal.valueOf(10_000),
                1L,
                menuProducts
        );

        //when
        final Menu menu = service.create(menuRequest);
        em.flush();
        em.clear();

        //then
        assertSoftly(softly -> {
            Optional<Menu> menuOptional = menuRepository.findById(menu.getId());
            assertThat(menuOptional).isNotEmpty();
            assertThat(menuOptional.get().getMenuProducts()).hasSize(2);
        });
    }
}
