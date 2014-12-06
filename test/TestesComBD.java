import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import java.util.List;

import models.Episodio;
import models.Serie;
import models.Temporada;
import models.bd.BD;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.Helpers;
import play.twirl.api.Html;
import scala.Option;
import views.html.index;

import javax.persistence.EntityManager;

public class TestesComBD {
	BD bd = new BD();
	Serie serie1 = new Serie("Arrow");
	Serie serie2 = new Serie("Chuck");
	Temporada temporada1 = new Temporada(1);
	Temporada temporada2 = new Temporada(1);
	Episodio episodio1S1 = new Episodio("Pilot", 1);
	Episodio episodio2S1 = new Episodio("Honor Thy Father", 2);
	Episodio episodio3S1 = new Episodio("Lone Gunmen", 3);
	Episodio episodio4S1 = new Episodio("An Innocent Man", 4);
	Episodio episodio1S2 = new Episodio("Chuck Versus the Intersect", 1);
	Episodio episodio2S2 = new Episodio("Chuck Versus the Helicopter", 2);
	Episodio episodio3S2 = new Episodio("Chuck Versus the Tango", 3);
	Episodio episodio4S2 = new Episodio("Chuck Versus the Wookiee", 4);
	List<Serie> series;
	public EntityManager em;

	@Before
	public void setUp() {
		FakeApplication app = Helpers.fakeApplication(new GlobalSettings());
		Helpers.start(app);
		Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(
				JPAPlugin.class);
		em = jpaPlugin.get().em("default");
		JPA.bindForCurrentThread(em);
		em.getTransaction().begin();
	}

	@After
	public void tearDown() {
		em.getTransaction().commit();
		JPA.bindForCurrentThread(null);
		em.close();
	}

	@Test
	public void deveIniciarSemSeries() {
		series = bd.findAllByClass(Serie.class);
		assertThat(series.size() == 0).isTrue();
	}

	@Test
	public void deveSalvarSerieNoBD() {
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.size() == 1).isTrue();
		assertThat(series.get(0).getNome()).isEqualTo("Arrow");
		bd.persist(serie2);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.size() == 2).isTrue();
		assertThat(series.get(1).getNome()).isEqualTo("Chuck");
	}

	@Test
	public void deveSalvarTemporadaNoBD() {
		serie1.addTemporada(temporada1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadasTotal() == 1).isTrue();
		assertThat(
				series.get(0).getTemporadas().get(0).getEpisodios().size() == 0)
				.isTrue();
	}

	@Test
	public void deveSalvarEpisodioNoBD() {
		serie1.addTemporada(temporada1);
		temporada1.addEpisodio(episodio1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadas().get(0).getNumEpisodios() == 1)
				.isTrue();
		assertThat(
				series.get(0).getTemporadas().get(0).getEpisodios().get(0)
						.getNome()).isEqualTo("Pilot");
	}

	@Test
	public void deveSetarSerieParaAssistida() {
		serie1.addTemporada(temporada1);
		temporada1.addEpisodio(episodio1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).assistindo()).isFalse();
		serie1.setAssistida(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).assistindo()).isTrue();
	}

	@Test
	public void deveSetarEpisodioParaAssistido() {
		serie1.addTemporada(temporada1);
		temporada1.addEpisodio(episodio1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(0).getTemporadas().get(0).getEpisodios().get(0)
						.isAssistido()).isFalse();
		episodio1S1.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(0).getTemporadas().get(0).getEpisodios().get(0)
						.isAssistido()).isTrue();
	}

	@Test
	public void deveAtualizarEstadoDeTemporada() {
		serie1.addTemporada(temporada1);
		temporada1.addEpisodio(episodio1S1);
		temporada1.addEpisodio(episodio3S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadas().get(0).isNemComecei())
				.isTrue();
		episodio1S1.setAssistido(true);
		assertThat(series.get(0).getTemporadas().get(0).isAssistindo())
				.isTrue();
		episodio3S1.setAssistido(true);
		assertThat(series.get(0).getTemporadas().get(0).assisti()).isTrue();
		temporada1.addEpisodio(episodio2S1);
		assertThat(series.get(0).getTemporadas().get(0).isAssistindo())
				.isTrue();
	}

	@Test
	public void deveInformarProximoEpisodio() {
		serie1.addTemporada(temporada1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(0).getProximoEpisodio().getNome() == "Nenhum episódio assistido")
				.isTrue();
		
		temporada1.addEpisodio(episodio1S1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Pilot")
				.isTrue();

		temporada1.addEpisodio(episodio2S1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Pilot").isTrue();

		episodio1S1.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Honor Thy Father")
				.isTrue();
		
		temporada1.addEpisodio(episodio3S1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Honor Thy Father").isTrue();

	}

}