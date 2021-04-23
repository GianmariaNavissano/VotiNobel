package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<Esame>();
		soluzioneMigliore = new HashSet<Esame>();
		this.mediaSoluzioneMigliore = 0;
		cerca2(parziale, 0, numeroCrediti);
		//cerca1(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}
	//Complessità 2^N
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		//1) crediti >= m
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			if(media > this.mediaSoluzioneMigliore) {
				this.soluzioneMigliore = new HashSet<Esame>(parziale);
				mediaSoluzioneMigliore = media;
			}
			return;
		}
		//2) L=N -> non ci sono altri esami
		if(L==partenza.size()) 
			return;
		
		//generazione sottoproblemi
		
		//partenza[L] è da aggiungere o no?
		parziale.add(partenza.get(L));
		this.cerca2(parziale, L+1, m);
		
		parziale.remove(partenza.get(L));
		this.cerca2(parziale, L+1, m);
		
		
	}

	//Complessità N!
	private void cerca1(Set<Esame> parziale, int L, int m) {
		//casi terminali
		
		//1) crediti >= m
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		if(crediti == m) {
			double media = this.calcolaMedia(parziale);
			if(media > this.mediaSoluzioneMigliore) {
				this.soluzioneMigliore = new HashSet<Esame>(parziale);
				mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//2) L=N -> non ci sono altri esami
		if(L==partenza.size()) 
			return;
		
		//generazione sotto problemi
		for(Esame e : partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				this.cerca1(parziale, L+1, m);
				//back-tracking
				parziale.remove(e);
			}
		}
		
		
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
