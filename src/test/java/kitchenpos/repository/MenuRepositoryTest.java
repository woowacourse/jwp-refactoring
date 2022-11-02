package kitchenpos.repository;

import static kitchenpos.fixture.DomainFixture.createMenu;
import static kitchenpos.fixture.DomainFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(createMenuGroup());
    }

    @Test
    void 아이디_목록에_해당하는_요소의_수를_구한다() {
        Menu menu1 = menuRepository.save(createMenu(menuGroup));
        menuRepository.save(createMenu(menuGroup));

        assertThat(menuRepository.countByIdIn(List.of(menu1.getId()))).isEqualTo(1);
    }
}
