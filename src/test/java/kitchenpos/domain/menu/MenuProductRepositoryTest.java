package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("menuId로 MenuProduct목록을 조회한다.")
    void findAllByMenuId() {
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());

        assertAll(
                () -> assertThat(menuProducts).extracting("menuId").containsOnly(menu.getId()),
                () -> assertThat(menuProducts).hasSize(2)
        );
    }
}