package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
    //punto 1
	private Graph <String , DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	//punto 2
	private List<String> best;
	
	
	public Model() {
		dao=new EventsDao();
	}

//PUNTO 1 : CREAZIONE GRAFO
	
	public void creaGrafo(String categoria, int mese) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		//aggiunta archi
		for(Adiacenza a :dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(),a.getPeso());
		}
		System.out.println("Grafo creato");
		System.out.println("#Vertici: "+this.grafo.vertexSet().size());
		System.out.println("#Archi: "+this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio(){
		//calcolo il PESO MEDIO scorrendo tutti gli archi
		double pesoTot=0.0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			pesoTot+=this.grafo.getEdgeWeight(e);
		}
		double avg=pesoTot / this.grafo.edgeSet().size();
		System.out.println("PESO MEDIO: "+avg);
		//prendo archi con peso maggiore di avg
		List<Adiacenza> result= new ArrayList<>();
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>avg)
				result.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e),
						(int)this.grafo.getEdgeWeight(e)));
		}
		return result;
	}
	
//PUNTO 2 :RICORSIONE
	
	public List<String> calcolaPercorso(String sorgente, String destinazione){
		best= new LinkedList<>();
		List <String> parziale= new LinkedList<>();
		parziale.add(sorgente);
		cerca(parziale,destinazione); //livello0 =sorgente
		return best;
	}

    private void cerca(List<String> parziale, String destinazione) {
    	//terminazione
    	if(parziale.get(parziale.size()-1).equals(destinazione)) {
    		if(parziale.size()>best.size())
    			best=new LinkedList<>(parziale);
    		return;
    	}
    	
    	//algoritmo ricorsivo:scorro i vicini dell'ultimo inserito e provo tutte le strade
    	for(String vicino: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
    		if(!parziale.contains(vicino)) {
    			parziale.add(vicino);
    		    cerca(parziale, destinazione);
    		    parziale.remove(parziale.size()-1);
    		}
    	}
	
    }
	
	
	
	
	
	
	
}
