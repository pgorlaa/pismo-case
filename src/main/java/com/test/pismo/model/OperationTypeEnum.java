package com.test.pismo.model;

public enum OperationTypeEnum {

    A_VISTA(new OperationType(1L, "COMPRA A VISTA", true)),
    PARCELADA(new OperationType(2L, "COMPRA PARCELADA", true)),
    SAQUE(new OperationType(3L, "SAQUE", true)),
    PAGAMENTO(new OperationType(4L, "PAGAMENTO", false));

    private final OperationType operationType;

    OperationTypeEnum(OperationType operationType) {
        this.operationType = operationType;
    }

    public OperationType getOperation() {
        return operationType;
    }
}
