package models;

import com.google.common.base.Objects;

import javax.persistence.*;

/**
 * @author Tatiana Saturno
 *
 */

//Classe onde a meta poder ser adicionada, removida ou mudada

@Entity
public class Episodio implements Comparable<Episodio>{


    @Id
    @GeneratedValue
    private Long id;

    private int numEpisodio;
    private String nome;
    private boolean assisti;

    public Episodio(){
    	assisti = false;
    }

    /**
     * Construtor de meta
     * @param numTemporada
     * @param descricao
     * @param prioridade
     */
    public Episodio(int numEpisodio, String nome){
        this();
        this.numEpisodio = numEpisodio;
        this.nome = nome;
    }

    /**
     * @return id
     */
    public Long getId(){
        return id;
    }

    public int getNumEpisodio() {
		return numEpisodio;
	}

	public String getNome() {
		return nome;
	}

	public boolean assisti() {
		return assisti;
	}
	
	public void episodioAssistido(){
		assisti = true;
	}

	@Override
    public int compareTo(Episodio outra) {
    	//ordenar episodios por ordem alfabetica
        int resultado = 0;
        if(outra.getNumEpisodio() < outra.getNumEpisodio()) resultado = -1;
        else if(outra.getNumEpisodio() > outra.getNumEpisodio()) resultado = 1;
		return resultado ;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Episodio))
            return false;

        Episodio episodio = (Episodio) obj;
        return episodio.getNome().equals(this.getNome());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.numEpisodio);
    }


}
