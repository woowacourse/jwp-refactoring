package fixture;

public enum MenuGroupFixtures {

    두마리메뉴_그룹(1L, "두마리메뉴"),
    한마리메뉴_그룹(2L, "한마리메뉴"),
    순살파닭두마리메뉴_그룹(3L, "순살파닭두마리메뉴"),
    신메뉴_그룹(4L, "신메뉴");

    private final long id;
    private final String name;

    MenuGroupFixtures(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long id() {
        return id;
    }

    public String 이름() {
        return name;
    }
}
