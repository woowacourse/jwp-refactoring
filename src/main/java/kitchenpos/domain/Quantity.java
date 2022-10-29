package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long volume;

    protected Quantity() {
    }

    public Quantity(long volume) {
        validatePositive(volume);
        this.volume = volume;
    }

    private void validatePositive(long volume) {
        if (volume <= 0) {
            throw new IllegalArgumentException("수량은 0개 이하일 수 없습니다.");
        }
    }

    public long getVolume() {
        return volume;
    }
}
