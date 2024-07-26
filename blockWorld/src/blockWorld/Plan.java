package blockWorld;

public class Plan {
    String method;
    Block blockTo;
    Block blockOn;

    Plan(String method, Block blockTo){
        this.method = method;
        this.blockTo = blockTo;
    }

    Plan(String method, Block blockOn, Block blockTo){
        this.method = method;
        this.blockTo = blockTo;
        this.blockOn = blockOn;
    }

    public String getPlan1String() {
        if(blockOn != null) {
            return this.method + "(" + blockTo.name + "," + blockOn.name + ")";
        }
        return this.method + "(" + blockTo.name + ")";
    }

    public String getPlan2String() {
        if(blockOn != null) {
            return this.method + "(" + blockTo.name + "," + blockOn.name + ")";
        }
        return this.method + "(" + blockTo.name + ")";
    }
}
