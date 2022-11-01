package kitchenpos.support.fixture;

public enum MenuGroupFixture {

    두마리메뉴(1L, "두마리메뉴"),
    한마리메뉴(2L, "한마리메뉴"),
    ;

    private final long id;
    private final String name;

    MenuGroupFixture(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
