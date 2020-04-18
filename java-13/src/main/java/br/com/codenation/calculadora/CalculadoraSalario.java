package br.com.codenation.calculadora;


public class CalculadoraSalario {

	public long calcularSalarioLiquido(double salarioBase) {
		if(salarioValido(salarioBase)) return Math.round(salarioBase - contabilizaImpostos(salarioBase));
		return 0;
	}

	private double contabilizaImpostos(double salarioBase){
		return calcularInss(salarioBase) + calcularIrrf(salarioBase);
	}

	private double calcularInss(double salarioBase) {
		double valorDesconto =0;

		if(salarioBase <=1500)	valorDesconto = salarioBase * 0.08;
		if(salarioBase > 1500 && salarioBase <=4000) valorDesconto = salarioBase * 0.09;
		if (salarioBase > 4000)valorDesconto = salarioBase*0.11;

		return valorDesconto;
	}

	private double calcularIrrf(double salarioBase) {
		double salarioComInss = salarioBase - calcularInss(salarioBase);
		double valorDesconto =0;

		if(salarioComInss <=3000)	valorDesconto = 0;
		if(salarioComInss > 3000 && salarioComInss <= 6000) valorDesconto = salarioComInss * 0.075;
		if(salarioComInss > 6000) valorDesconto = salarioComInss * 0.15;

		return valorDesconto;
	}

	private boolean salarioValido(double salarioBase){
		return salarioBase > 1039;
	}
}
