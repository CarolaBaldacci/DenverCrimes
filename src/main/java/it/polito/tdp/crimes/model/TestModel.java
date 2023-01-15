package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		Model m= new Model();
		m.creaGrafo("drug-alcohol", 2);
		System.out.println(m.getArchiMaggioriPesoMedio());
		
	}

}
