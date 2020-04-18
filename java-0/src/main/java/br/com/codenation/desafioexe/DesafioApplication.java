package br.com.codenation.desafioexe;

import java.util.ArrayList;
import java.util.List;

public class DesafioApplication {

	public static List<Integer> fibonacci() {

		List<Integer> numerosFibonacci = new ArrayList<>();

		numerosFibonacci.add(0,0);
		numerosFibonacci.add(1,1);

		Integer controle = numerosFibonacci.get(0) + numerosFibonacci.get(1);

		do{

			numerosFibonacci.add(controle);

			controle = numerosFibonacci.get(numerosFibonacci.size()-1) + numerosFibonacci.get(numerosFibonacci.size()-2);

		}while (controle < 380);

		return numerosFibonacci;
	}

	public static Boolean isFibonacci(Integer a) {

		return fibonacci().contains(a);
	}

}

