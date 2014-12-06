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
@Entity(name = "Serie")
public class Serie implements Comparable<Serie> {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String nome;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "TEMPORADA")
	private List<Temporada> temporadas;
	@Column
	private boolean assistindo;

	public Serie() {
		this.temporadas = new ArrayList<Temporada>();
	}

	/**
	 * Construtor da série que também inicializa a lista temporadas
	 * @param nome
	 */
	public Serie(String nome) {
		this();
		this.nome = nome;
	}
	
	public boolean isEmpty(){
		return temporadas.size() == 0;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Temporada> getTemporadas() {
		return temporadas;
	}

	/**
	 * Adiciona temporada
	 * @param temporada
	 */
	public void addTemporada(Temporada temporada) {
		this.temporadas.add(temporada);
	}

	/**
	 * @return verifica se está assistindo a série
	 */
	public boolean assistindo() {
		return assistindo;
	}

	/**
	 * @param acompanhando
	 */
	public void setAssistida(boolean acompanhando) {
		this.assistindo = acompanhando;
	}

	public int getTemporadasTotal() {
		return temporadas.size();
	}

	public Temporada getUltimaTemporada() throws Exception {
		return temporadas.get(temporadas.size() - 1);
	}

	/**
	 * @return próximo episódio com base no número de episódios assistidos
	 */
	public Episodio getProximoEpisodio() {
		//Vale ressaltar que esse método segue a premissa de que
		//o usuário assista episódios na sequência correta (1, 2, 3, ..., n)
		for (int k = this.getTemporadasTotal(); k > 0; k--) {
			
			List<Episodio> episodios = this.getTemporadas().get(k - 1)
					.getEpisodios();
			for (int i = episodios.size(); i > 0; i--) {
				if (episodios.get(i - 1).isAssistido()) {
					if(this.getTemporadas().get(k - 1).assisti()) return new Episodio(
							"Último episódio assistido", 0);
						return episodios.get(i);
				}
			}
		}
		return new Episodio("Nenhum episódio assistido", 0);
	}
	
	public Temporada getTemporadaPeloIndice(int i){
		return temporadas.get(i);
	}

	@Override
	public int compareTo(Serie outra) {
		// ordenar serie por ordem alfabetica
		return this.getNome().compareToIgnoreCase(outra.getNome());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Serie))
			return false;

		Serie outra = (Serie) obj;
		return outra.getNome().equals(this.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getNome());
	}
}
