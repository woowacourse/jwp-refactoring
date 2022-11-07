package kitchenpos.common.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.domain.menu.Menu;
import kitchenpos.common.domain.menu.MenuHistory;
import kitchenpos.common.domain.menu.Price;
import kitchenpos.common.repository.menu.MenuHistoryRepository;
import kitchenpos.common.repository.menu.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MenuHistoryRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuHistoryRepository menuHistoryRepository;

    @Test
    void findFirstByMenuOrderByCreatedTimeDesc_메서드는_가장_최신버전의_메뉴_정보를_반환한다() {
        Menu menu = new Menu(null, "메뉴", new Price(BigDecimal.valueOf(1000)), 1L);
        menu = menuRepository.save(menu);
        menuHistoryRepository.save(MenuHistory.of(menu));
        menu.changeName("신메뉴");
        menu = menuRepository.save(menu);

        MenuHistory expected = menuHistoryRepository.save(MenuHistory.of(menu));
        MenuHistory actual = menuHistoryRepository.findFirstByMenuOrderByCreatedTimeDesc(menu).get();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
