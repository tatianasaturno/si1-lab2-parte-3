package models;

import com.google.common.base.Objects;

import javax.persistence.*;

/**
 * @author Tatiana Saturno
 *
 */

//Classe onde a meta poder ser adicionada, removida ou mudada

@Entity
public class Meta implements Comparable<Meta>{

    public enum Prioridade{
        ALTA(1), NORMAL(0), BAIXA(-1);

        private int value;

        Prioridade(int newValue){
            value = newValue;
        }

        public int getNumericValue(){
            return value;
        }
    }

    @Id
    @GeneratedValue
    private Long id;

    private String nome;
    private String descricao;
    private Prioridade prioridade;
    private boolean metaAlcancada;

    public Meta(){
        metaAlcancada = false;
    }

    /**
     * Construtor de meta
     * @param nome
     * @param descricao
     * @param prioridade
     */
    public Meta(String nome, String descricao, Prioridade prioridade){
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.prioridade = prioridade;
    }

    /**
     * @return id
     */
    public Long getId(){
        return id;
    }

    /**
     * @param nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     * @return nome
     */
    public String getNome(){
    	return nome;
    }

    /**
     * @param descricao
     */
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    /**
     * @return descricao da meta
     */
    public String getDescricao(){
        return descricao;
    }

    /**
     * @param prioridadeDada
     */
    public void setPrioridade(String prioridadeDada){
        switch (prioridadeDada){
            case "alta":
                prioridade = Prioridade.ALTA;
                break;
            case "normal":
                prioridade = Prioridade.NORMAL;
                break;
            default:
                prioridade = Prioridade.BAIXA;
        }
    }

    public Prioridade getPrioridade(){
        return prioridade;
    }

    public void setMetaAlcancada(boolean status){
        metaAlcancada = status;
    }

    public boolean getAlcancada(){
        return metaAlcancada;
    }

    @Override
    public int compareTo(Meta outra) {
    	//ordenar semana por prioridade
        return outra.getPrioridade().getNumericValue() - this.getPrioridade().getNumericValue();
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Meta))
            return false;

        Meta meta2 = (Meta) obj;
        return meta2.getNome().equals(this.getNome());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.nome);
    }


}
