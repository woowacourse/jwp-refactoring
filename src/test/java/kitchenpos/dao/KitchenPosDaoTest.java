package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_MENU_GROUP_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    protected Long getCreatedMenuGroupId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Long savedMenuGroupId = savedMenuGroup.getId();
        assertThat(savedMenuGroupId).isNotNull();
        return savedMenuGroupId;
    }
}
