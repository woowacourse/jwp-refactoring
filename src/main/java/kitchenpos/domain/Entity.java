package kitchenpos.domain;

public interface Entity {

    boolean isNew();

    void validateOnCreate();
}
