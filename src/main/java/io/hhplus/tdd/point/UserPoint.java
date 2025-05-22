package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    public UserPoint {
        validate(id, point);
    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint charge(long amount) {
        return new UserPoint(this.id, this.point + amount, System.currentTimeMillis());
    }

    private static void validate(long id, long point){
        if(point < 0) {
            throw new IllegalArgumentException("충전할 수 있는 최소 금액은 0 이상입니다.");
        }
    }

    public UserPoint use(long amount) {
        if(this.point > amount) {
            return new UserPoint(this.id, this.point - amount, System.currentTimeMillis());
        } else if (this.point == 0){
            throw new IllegalArgumentException("포인트가 없습니다. 충전해주세요");
        } else {
            throw new IllegalArgumentException("포인트가 부족합니다. " + this.point + "이하로 사용가능합니다");
        }
    }
}
