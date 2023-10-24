package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.CreateMenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupServiceFixture {

    protected CreateMenuGroupRequest 생성할_메뉴_그룹;
    protected MenuGroup 저장된_메뉴_그룹;
    protected List<MenuGroup> 저장된_메뉴_그룹_리스트;
    protected MenuGroup 저장된_메뉴_그룹_1 = new MenuGroup();
    protected MenuGroup 저장된_메뉴_그룹_2 = new MenuGroup();

    @BeforeEach
    void setUp() {
        생성할_메뉴_그룹 = new CreateMenuGroupRequest("추천 메뉴");
        저장된_메뉴_그룹 = new MenuGroup(생성할_메뉴_그룹.getName());

        저장된_메뉴_그룹_리스트 = List.of(저장된_메뉴_그룹_1, 저장된_메뉴_그룹_2);
    }
}
