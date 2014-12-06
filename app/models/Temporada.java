package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.google.common.base.Objects;

/**
 * @author Tatiana Saturno da Silva
 *
 */

@Entity(name = "Temporada")
public class Temporada implements Comparable<Temporada> {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private int numero;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "EPISODIO")
	List<Episodio> episodios;

	@Column
	private int assistida = -1;

	@Column
	private int totalDeEpisodiosAssistidos;

	private final int ASSISTI = 1;
	private final int ASSISTINDO = 0;
	private final int NEMCOMECEI = -1;
	private boolean assistindoForaDeOrdem;

	public Temporada() {
		this.episodios = new ArrayList<Episodio>();
		assistindoForaDeOrdem = false;
		totalDeEpisodiosAssistidos = 0;
	}

	public Temporada(int numero) {
		this();
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public List<Episodio> getEpisodios() {
		return episodios;
	}

	public void addEpisodio(Episodio episodio) {
		this.episodios.add(episodio);
		checarSeAssistida();
	}

	public int getAssistida() {
		return assistida;
	}

	public int getNumEpisodios() {
		return episodios.size();
	}

	public int getTotalDeEpisodiosAssistidos() {
		return totalDeEpisodiosAssistidos;
	}

	public Episodio getEpisodioPeloIndice(int i) {
		return episodios.get(i);
	}

	public void setTotalDeEpisodiosAssistidos(int totalDeEpisodiosAssistidos) {
		this.totalDeEpisodiosAssistidos = totalDeEpisodiosAssistidos;
	}

	public void setAssistida(int assistida) {
		if (assistida >= -1 && assistida <= 1)
			this.assistida = assistida;
	}

	public boolean assisti() {
		checarSeAssistida();

		boolean resultado = false;
		if (getAssistida() == ASSISTI)
			resultado = true;
		return resultado;
	}

	/**
	 * @return se está assistindo a temporada
	 */
	public boolean isAssistindo() {
		checarSeAssistida();

		boolean resultado = false;
		if (getAssistida() == ASSISTINDO)
			resultado = true;
		return resultado;
	}

	/**
	 * @return se não viu nenhum episódio da série
	 */
	public boolean isNemComecei() {
		checarSeAssistida();

		boolean resultado = false;
		if (getAssistida() == NEMCOMECEI)
			resultado = true;
		return resultado;
	}
	
	/**
	 * @param numEpisodio 
	 * @return se o usuário está assistindo episódios fora da ordem (1,2,3,...,n)
	 */
	public boolean isAssistindoForaDeOrdem(int numEpisodio){
		foraDeOrdem(numEpisodio);
		return assistindoForaDeOrdem;
	}
	
	

	public void setAssistindoForaDeOrdem(boolean assistindoForaDeOrdem) {
		this.assistindoForaDeOrdem = assistindoForaDeOrdem;
	}

	/**
	 * Checa se foi assistida
	 */
	public void checarSeAssistida() {
		int contador = 0;
		for (int i = 0; i < episodios.size(); i++) {
			if (episodios.get(i).isAssistido()) {
				contador += 1;
			}
		}

		totalDeEpisodiosAssistidos = contador;
		if (contador == episodios.size())
			setAssistida(ASSISTI);
		else if (contador > 0)
			setAssistida(ASSISTINDO);
		else
			setAssistida(NEMCOMECEI);
	}
	
	private void foraDeOrdem(int numEpisodio){
		//checa se episódio foi visto fora de ordem (1, 2, 3, ..., n)
		setAssistindoForaDeOrdem(numEpisodio > getTotalDeEpisodiosAssistidos()); 
	}

	@Override
	public int compareTo(Temporada outra) {
		// ordenar temporadas por número
		int resultado = 0;
		if (this.getNumero() < outra.getNumero())
			resultado = -1;
		else if (this.getNumero() > outra.getNumero())
			resultado = 1;
		return resultado;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getNumero(),
				this.getNumEpisodios());
	}
}
