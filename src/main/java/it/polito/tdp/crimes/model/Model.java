package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
    
	private Graph <String , DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	public Model() {
		dao=new EventsDao();
	}
	
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
	
	
	
}
