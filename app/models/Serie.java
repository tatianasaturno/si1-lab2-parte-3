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

	public boolean assistindo() {
		return assistindo;
	}

	public void setAssistida(boolean acompanhando) {
		this.assistindo = acompanhando;
	}

	public int getTemporadasTotal() {
		return temporadas.size();
	}

	public Temporada getUltimaTemporada() throws Exception {
		return temporadas.get(temporadas.size() - 1);
	}

	public Episodio getProximoEpisodio() {
		//Vale ressaltar que esse método segue a premissa de que
		//o usuário assista episódios na sequência correta (1, 2, 3, ..., n)
		Episodio epProximo = new Episodio("Nenhum episódio assistido", 0);;
			if(this.getTemporadasTotal() > 0){ //checando se a temporada tem mais de
				for(Temporada t: this.getTemporadas()){
					if(t.getNumEpisodios() > 0){
						if(t.isAssistindo()){
							epProximo = t.getEpisodioPeloIndice(t.getTotalDeEpisodiosAssistidos());
						}
						else if(t.assisti()){
							epProximo = new Episodio("Último episódio assistido", 0);
						}else{
							epProximo = t.getEpisodioPeloIndice(0);
						}
						
					}
			}
			}
		
		return epProximo;
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
