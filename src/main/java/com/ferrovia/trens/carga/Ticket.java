package com.ferrovia.trens.carga;

public class Ticket {
	private float valor;
	
	public Ticket(float valor) {
		this.valor = valor;
	}
	
	public float getValor(){
		return valor;
	}
	
	public void setValor(float valor){
		this.valor = valor;
	}
}
