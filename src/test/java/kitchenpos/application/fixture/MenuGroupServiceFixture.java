package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupServiceFixture {

    protected String 메뉴_그룹_이름 = "메뉴 그룹";
    protected MenuGroup 저장된_메뉴_그룹;
    protected List<MenuGroup> 저장된_메뉴_그룹들;
    protected MenuGroup 저장된_메뉴_그룹1;
    protected MenuGroup 저장된_메뉴_그룹2;
    protected MenuGroup 저장된_메뉴_그룹3;

    @BeforeEach
    void setUp() {
        저장된_메뉴_그룹 = new MenuGroup(메뉴_그룹_이름);
        저장된_메뉴_그룹.updateId(1L);

        저장된_메뉴_그룹1 = new MenuGroup("메뉴 그룹1");
        저장된_메뉴_그룹1.updateId(1L);
        저장된_메뉴_그룹2 = new MenuGroup("메뉴 그룹2");
        저장된_메뉴_그룹2.updateId(2L);
        저장된_메뉴_그룹3 = new MenuGroup("메뉴 그룹3");
        저장된_메뉴_그룹3.updateId(3L);
        저장된_메뉴_그룹들 = List.of(저장된_메뉴_그룹1, 저장된_메뉴_그룹2, 저장된_메뉴_그룹3);
    }
}
