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
    @Column
    private String nome;
    private int numTemporadas;
    private int temporadasAssistidas;
    
    @OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="TEMPORADAS")
    private List<Temporada> temporadas;
    @Column
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

	/**
	 * @return lista com as temporadas
	 */
	public List<Temporada> getTemporadas() {
		return temporadas;
	}
	
	public int getTotalTemporadas(){
		return getTemporadas().size();
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

    public Temporada getUltimaTemporada() throws Exception{
		if (getNumTemporadas()==0)
			throw new Exception("Lista de Temporadas vazia! Nome da serie: "+this.getNome());
		return temporadas.get(temporadas.size()-1);
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNumTemporadas(int numTemporadas) {
		this.numTemporadas = numTemporadas;
	}
	
	public void setTemporadasAssistidas(int temporadasAssistidas) {
		this.temporadasAssistidas = temporadasAssistidas;
	}

	public void setTemporadas(List<Temporada> temporadas) {
		this.temporadas = temporadas;
	}


}