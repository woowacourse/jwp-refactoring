package kitchenpos.repository;

import static kitchenpos.common.fixtures.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.fixtures.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.fixtures.MenuFixtures.MENU2_NAME;
import static kitchenpos.common.fixtures.MenuFixtures.MENU2_PRICE;
import static kitchenpos.common.fixtures.MenuGroupFixtures.MENU_GROUP1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("ID를 받아서 ID에 해당하는 행의 개수를 반환한다.")
    void countByIdIn() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(MENU_GROUP1());
        final Menu menu1 = new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup);
        final Menu menu2 = new Menu(MENU2_NAME, MENU2_PRICE, savedMenuGroup);
        final Menu savedMenu1 = menuRepository.save(menu1);
        final Menu savedMenu2 = menuRepository.save(menu2);
        final List<Long> ids = List.of(savedMenu1.getId(), savedMenu2.getId());

        // when
        int result = menuRepository.countByIdIn(ids);

        // then
        assertThat(result).isEqualTo(2);
    }
}
