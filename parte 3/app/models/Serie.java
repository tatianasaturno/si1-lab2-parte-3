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
public class Serie implements Comparable<Serie>{


    @Id
    @GeneratedValue
    private Long id;

    private String nome;
    private int numTemporadas;
    private int numDefinidoDeTemporadas; //caso seja definido em global
    private int temporadasAssistidas;
    private int proximaTemporada;
    
    @OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="TEMPORADAS")
    private List<Temporada> temporadas;
    
    private boolean assisti;
    private boolean assistindo;

    public Serie(){
    	assisti = false;
    	assistindo = true;
    	temporadas = new ArrayList<Temporada>();
    	temporadasAssistidas = 0;
    }
    
    public Serie(String nome){
        this();
        this.nome = nome;
        numTemporadas = 0;
        numDefinidoDeTemporadas = 1000;
    }
    
    public void addTemporada(Temporada t){
    	temporadas.add(t);
    	numTemporadas++;
    }

    /**
     * @return id
     */
    public Long getId(){
        return id;
    }

    /**
     * @return nome
     */
    public String getNome(){
    	return nome;
    }

    /**
     * @return numeros de temporadas da serie
     */
    public int getNumTemporadas() {
		return numTemporadas;
	}

	public int getTemporadasAssistidas() {
		return temporadasAssistidas;
	}

	public int getNumDefinidoDeTemporadas() {
		return numDefinidoDeTemporadas;
	}

	/**
	 * @return lista com as temporadas
	 */
	public List<Temporada> getTemporadas() {
		return temporadas;
	}
	
	public int getTotalTemporadas(){
		return getTemporadas().size();
	}
	
	public int getProximaTemporada() {
		int saida = 0;
		if(getTemporadasAssistidas() < getNumDefinidoDeTemporadas()){
			proximaTemporada = getTemporadasAssistidas()+1;
			saida = proximaTemporada;
		}
		return saida;
	}

	/**
	 * @return se assistiu a serie
	 */
	public boolean assisti() {
		return assisti;
	}
	
	public void serieAssistida(){
		assisti = true;
		assistindo = false;
	}
	
	public void assistiTemporada(int i){
		temporadasAssistidas++;
		temporadas.get(i-1).assisti();
	}
	
	public boolean assistindo(){
		return assistindo;
	}
	
	public boolean isEmpty(){
		return temporadas.isEmpty();
	}
	
	

	@Override
    public int compareTo(Serie outra) {
    	//ordenar serie por ordem alfabetica
        return outra.getNome().compareTo(this.getNome());
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Serie))
            return false;

        Serie serie = (Serie) obj;
        return serie.getNome().equals(this.getNome());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.nome);
    }

	public Temporada getUltimaTemporada() {
		return getTemporadas().get(getNumTemporadas() - 1);
	}


}
