package models;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

import javax.persistence.*;

/**
 * @author Tatiana Saturno
 *
 */

//Classe onde a meta poder ser adicionada, removida ou mudada

@Entity
public class Temporada implements Comparable<Temporada>{


    @Id
    @GeneratedValue
    private Long id;

    private int numTemporada;
    private int numEpisodios;
    private int proximoEpisodio;
    private int episodiosAssistidos;
    private int numDefinidoDeEpisodios; //caso seja definido em global
    
    @OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="EPISODIOS")
    private List<Episodio> episodios;
    
    private boolean assisti;
    private boolean assistindo;

    public Temporada(){
    	assisti = false;
    	assistindo = true;
    	episodios = new ArrayList<Episodio>();
    	numEpisodios = 0;
    	numDefinidoDeEpisodios = 1000;
    }
    
    public Temporada(int numTemporada){
        this();
        this.numTemporada = numTemporada;
        numTemporada = 0;
    }
    
    public void addEpisodio(Episodio e){
    	episodios.add(e);
    	numEpisodios++;
    }

    /**
     * @return id
     */
    public Long getId(){
        return id;
    }

    /**
     * @return numero da temporada
     */
    public int getNumTemporada(){
    	return numTemporada;
    }

    public int getNumDefinidoDeEpisodios() {
		return numDefinidoDeEpisodios;
	}

	public int getNumEpisodios() {
		return numEpisodios;
	}

	public int getProximoEpisodio() {
		int saida = 0;
		if(getEpisodiosAssistidos() < getNumDefinidoDeEpisodios()){
			proximoEpisodio = getEpisodiosAssistidos()+1;
			saida = proximoEpisodio;
		}
		return saida;
	}

	public List<Episodio> getEpisodios() {
		return episodios;
	}

	public void setNumEpisodios(int numEpisodios) {
		this.numEpisodios = numEpisodios;
	}

	public boolean assisti() {
		return assisti;
	}
	
	public void temporadaAssistida(){
		assisti = true;
		assistindo = false;
	}
	
	public void assistiEpisodio(int i){
		episodiosAssistidos++;
		episodios.get(i-1).assisti();
	}
	
	public boolean assistindo(){
		return assistindo;
	}
	
	public boolean isEmpty(){
		return episodios.isEmpty();
	}
	
	public int getEpisodiosAssistidos(){
		return episodiosAssistidos;
	}

	@Override
    public int compareTo(Temporada outra) {
    	//ordenar temporada por numero
        int resultado = 0;
        if(outra.getNumTemporada() < outra.getNumTemporada()) resultado = -1;
        else if(outra.getNumTemporada() > outra.getNumTemporada()) resultado = 1;
		return resultado ;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Temporada))
            return false;

        Temporada serie = (Temporada) obj;
        return serie.getNumTemporada() == this.getNumTemporada();
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.numTemporada);
    }


}
