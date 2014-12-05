package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;

@Entity(name="Episodio")
public class Episodio implements Comparable<Episodio>{
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String nome;
	@Column
	private int numero;
	@ManyToOne(cascade=CascadeType.ALL)
	Temporada temporada;
	@Column
	private boolean assistido;
	
	public Episodio(){
	}
	public Episodio(String nome, Temporada temporada, int numero){
		this.nome = nome;
		this.temporada = temporada;
		this.numero = numero;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
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
	public Temporada getTemporada() {
		return temporada;
	}
	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}
	public boolean isAssistido() {
		return assistido;
	}
	public void setAssistido(boolean assistido) {
		this.assistido = assistido;
		temporada.checarSeAssistida();
	}
	
	@Override
	public int compareTo(Episodio outra) {
		// ordenar temporadas por número
		int resultado = 0;
		if (this.getNumero() < outra.getNumero())
			resultado = -1;
		else if (this.getNumero() > outra.getNumero())
			resultado = 1;
		return resultado;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Episodio))
			return false;

		Episodio episodio = (Episodio) obj;
		return episodio.getNome().equals(this.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getNome());
	}
}