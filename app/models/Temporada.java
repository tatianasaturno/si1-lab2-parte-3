package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.base.Objects;

@Entity(name = "Temporada")
public class Temporada {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private int numero;
	
	@ManyToOne(cascade = CascadeType.ALL)
	Serie serie;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "EPISODIO")
	List<Episodio> episodios;
	
	@Column
	private int assistida = -1;

	private final int ASSISTI = 1;
	private final int ASSISTINDO = 0;
	private final int NEMCOMECEI = -1;

	public Temporada() {
		this.episodios = new ArrayList<Episodio>();
	}

	public Temporada(int numero, Serie serie) {
		this();
		this.numero = numero;
		this.serie = serie;
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

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
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

	public void setAssistida(int assistida) {
		if (assistida >= -1 && assistida <= 1)
			this.assistida = assistida;
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
		if (contador == episodios.size())
			setAssistida(ASSISTI);
		else if (contador > 0)
			setAssistida(ASSISTINDO);
		else
			setAssistida(NEMCOMECEI);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Temporada))
			return false;

		Temporada temporada = (Temporada) obj;
		return temporada.getNumero() == this.getNumero()
				&& temporada.getSerie().equals(this.getSerie())
				&& temporada.getNumEpisodios() == this.getNumEpisodios();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getSerie(), this.getNumero(),
				this.getNumEpisodios());
	}
}
