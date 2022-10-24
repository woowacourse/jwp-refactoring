package fixture;

public enum ProductFixtures {

    후라이드_상품(1L, "후라이드", 16000),
    양념치킨_상품(2L, "양념치킨", 16000),
    반반치킨_상품(3L, "반반치킨", 16000),
    통구이_상품(4L, "통구이", 16000),
    간장치킨_상품(5L, "간장치킨", 17000),
    순살치킨_상품(6L, "순살치킨", 17000);

    private final long id;
    private final String name;
    private final int price;

    ProductFixtures(long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long id() {
        return id;
    }

    public String 이름() {
        return name;
    }

    public int 가격() {
        return price;
    }
}
