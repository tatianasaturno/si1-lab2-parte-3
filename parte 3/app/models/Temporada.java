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
    @Column
    private int numTemporada;
    private int numEpisodios;
    private int proximoEpisodio;
    
    @OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="EPISODIOS")
    private List<Episodio> episodios;
    @ManyToOne(cascade=CascadeType.ALL)
	Serie serie;
    @Column
    private boolean assisti;
    private boolean assistindo;

    public Temporada(){
    	assisti = false;
    	assistindo = true;
    	episodios = new ArrayList<Episodio>();
    	numEpisodios = 0;
    }
    
    public Temporada(int numTemporada, Serie serie){
    	this();
		this.numTemporada = numTemporada;
		this.serie = serie;
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

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public int getNumEpisodios() {
		return numEpisodios;
	}

	private void achaProximoEpisodio(int contador) {
			proximoEpisodio = contador+1;
	}
	
	public int getProximoEpisodio(){
		checarAssistida();
		return proximoEpisodio;
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
	
	public boolean assistindo(){
		return assistindo;
	}
	
	public boolean isEmpty(){
		return episodios.isEmpty();
	}
	
	public void checarAssistida() {
		int contador = 0;
		for (Episodio episodio : episodios) {
			if (episodio.assisti()){
				contador+=1;
			}				
		}
		if (contador==episodios.size()) temporadaAssistida();
		else achaProximoEpisodio(contador);
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

	public boolean isAssisti() {
		return assisti;
	}

	public void setAssisti(boolean assisti) {
		this.assisti = assisti;
	}

	public boolean isAssistindo() {
		return assistindo;
	}

	public void setAssistindo(boolean assistindo) {
		this.assistindo = assistindo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNumTemporada(int numTemporada) {
		this.numTemporada = numTemporada;
	}

	public void setProximoEpisodio(int proximoEpisodio) {
		this.proximoEpisodio = proximoEpisodio;
	}

	public void setEpisodios(List<Episodio> episodios) {
		this.episodios = episodios;
	}


}
