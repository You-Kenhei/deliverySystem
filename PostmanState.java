/**
 * 配達員の状態のEnum
 */
public enum PostmanState {
    AVAILABLE("待機中", 0),
    UNAVAILABLE("休憩中or配送中", 1);


    PostmanState(String stateName, int value) {
    }


}
