package sample.model;

public class Reference {
    private int refenceID;
    private String referenceType;
    private String referenceValue;

    public Reference() {
    }

    public Reference(int refenceID, String referenceType, String referenceValue) {
        this.refenceID = refenceID;
        this.referenceType = referenceType;
        this.referenceValue = referenceValue;
    }

    public int getRefenceID() {
        return refenceID;
    }

    public void setRefenceID(int refenceID) {
        this.refenceID = refenceID;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    @Override
    public String toString() {
        return refenceID+"~"+referenceType +"~"+  referenceValue ;
    }
}
