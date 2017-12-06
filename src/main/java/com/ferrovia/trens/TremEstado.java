package com.ferrovia.trens;

public enum TremEstado {
    MOVING("Em Movimento"),
    STOPPED("Parado na estação"),
    WAITING("Esperando");

    private final String descricao;

    private TremEstado(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
