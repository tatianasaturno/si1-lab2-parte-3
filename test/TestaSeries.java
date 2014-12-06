import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import models.Episodio;
import models.Serie;
import models.Temporada;
import models.bd.BD;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;
import views.html.index;

import javax.persistence.EntityManager;

public class TestaSeries {
	BD bd = new BD();
	Serie serie1 = new Serie("Arrow");
	Serie serie2 = new Serie("Chuck");
	
	Temporada temporada1S1 = new Temporada(1);
	Temporada temporada1S2 = new Temporada(1);
	
	Temporada temporada2S1 = new Temporada(2);
	Temporada temporada2S2 = new Temporada(2);
	
	Episodio episodio1S1 = new Episodio("Pilot", 1);
	Episodio episodio2S1 = new Episodio("Honor Thy Father", 2);
	Episodio episodio3S1 = new Episodio("Lone Gunmen", 3);
	Episodio episodio1S1T2 = new Episodio("City of Heroes", 1);
	
	
	Episodio episodio1S2 = new Episodio("Chuck Versus the Intersect", 1);
	Episodio episodio2S2 = new Episodio("Chuck Versus the Helicopter", 2);
	Episodio episodio3S2 = new Episodio("Chuck Versus the Tango", 3);
	Episodio episodio1S2T2 = new Episodio("Chuck Versus the First Date", 1);
	
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
		serie1.addTemporada(temporada1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadasTotal() == 1).isTrue();
		assertThat(
				series.get(0).getTemporadaPeloIndice(0).getEpisodios().size() == 0)
				.isTrue();
	}

	@Test
	public void deveSalvarEpisodioNoBD() {
		serie1.addTemporada(temporada1S1);
		temporada1S1.addEpisodio(episodio1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadaPeloIndice(0).getNumEpisodios() == 1)
				.isTrue();
		assertThat(
				series.get(0).getTemporadaPeloIndice(0).getEpisodioPeloIndice(0)
						.getNome()).isEqualTo("Pilot");
	}

	@Test
	public void deveSetarSerieParaAssistindo() {
		serie1.addTemporada(temporada1S1);
		temporada1S1.addEpisodio(episodio1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).assistindo()).isFalse();
		serie1.setAssistindo(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).assistindo()).isTrue();
	}

	@Test
	public void deveSetarEpisodioParaAssistido() {
		serie1.addTemporada(temporada1S1);
		temporada1S1.addEpisodio(episodio1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(0).getTemporadaPeloIndice(0).getEpisodioPeloIndice(0)
						.isAssistido()).isFalse();
		assertThat(
				series.get(0).getTemporadaPeloIndice(0).getTotalDeEpisodiosAssistidos() == 0).isTrue();
		episodio1S1.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(0).getTemporadaPeloIndice(0).getEpisodioPeloIndice(0)
						.isAssistido()).isTrue();
	}

	@Test
	public void deveAtualizarEstadoDeTemporada() {
		serie1.addTemporada(temporada1S1);
		temporada1S1.addEpisodio(episodio1S1);
		temporada1S1.addEpisodio(episodio3S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadaPeloIndice(0).isNemComecei())
				.isTrue();
		episodio1S1.setAssistido(true);
		assertThat(series.get(0).getTemporadaPeloIndice(0).isAssistindo())
				.isTrue();
		episodio3S1.setAssistido(true);
		assertThat(series.get(0).getTemporadaPeloIndice(0).assisti()).isTrue();
		temporada1S1.addEpisodio(episodio2S1);
		assertThat(series.get(0).getTemporadaPeloIndice(0).isAssistindo())
				.isTrue();
	}

	@Test
	public void deveInformarProximoEpisodio() {
		serie1.addTemporada(temporada1S1);
		bd.persist(serie1);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(0).getProximoEpisodio().getNome() == "Nenhum episódio assistido")
				.isTrue();
		
		temporada1S1.addEpisodio(episodio1S1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Nenhum episódio assistido")
				.isTrue();
		
		episodio1S1.setAssistido(true);
		temporada1S1.addEpisodio(episodio2S1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Honor Thy Father")
				.isTrue();

		episodio2S1.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		temporada1S1.addEpisodio(episodio3S1);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Lone Gunmen").isTrue();
		
		episodio3S1.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Último episódio assistido")
				.isTrue();
		
		serie2.addTemporada(temporada1S2);
		bd.persist(serie2);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(1).getProximoEpisodio().getNome() == "Nenhum episódio assistido")
				.isTrue();
		
		temporada1S2.addEpisodio(episodio1S2);
		temporada1S2.addEpisodio(episodio2S2);
		temporada1S2.addEpisodio(episodio3S2);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(1).getProximoEpisodio().getNome() == "Nenhum episódio assistido")
				.isTrue();
		
		//Verificando se ele continua indicando o próximo episódio como sendo o logo após o assistido
		episodio2S2.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(1).getProximoEpisodio().getNome() == "Chuck Versus the Tango")
				.isTrue();
		
		episodio3S2.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(
				series.get(1).getProximoEpisodio().getNome() == "Último episódio assistido")
				.isTrue();
	}
	
	@Test
	public void deveSetarSerieComoAssistida(){
		//primeiro setar as duas séries como assistindo
		serie1.setAssistindo(true);
		serie2.setAssistindo(true);
		
		serie1.addTemporada(temporada1S1);
		bd.persist(serie1);
		temporada1S1.addEpisodio(episodio1S1);
		episodio1S1.setAssistido(true);
		temporada1S1.addEpisodio(episodio2S1);
		episodio2S1.setAssistido(true);
		temporada1S1.addEpisodio(episodio3S1);
		episodio3S1.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		assertThat(series.get(0).getProximoEpisodio().getNome() == "Último episódio assistido")
				.isTrue();
		//a série é assistida se possui todos os episódios de todas temporadas assistidos
		assertThat(series.get(0).assisti()).isTrue();
		//e muda o status assistindo
		assertThat(series.get(0).assistindo()).isFalse();
		
		serie2.addTemporada(temporada1S2);
		bd.persist(serie2);		
		temporada1S2.addEpisodio(episodio1S2);
		temporada1S2.addEpisodio(episodio2S2);
		temporada1S2.addEpisodio(episodio3S2);
		episodio2S2.setAssistido(true);		
		episodio3S2.setAssistido(true);
		series = bd.findAllByClass(Serie.class);
		//como falta assisti o episódio 1, a série não foi assistida
		assertThat(series.get(1).assisti()).isFalse();
		//continua o status de assistindo
		assertThat(series.get(1).assistindo()).isTrue();
		
		serie1.addTemporada(temporada2S1);
		bd.persist(serie1);
		//como foi adicionada uma nova temporada, porém esta não possui episódios
		//deve continuar como série assistida
		assertThat(series.get(0).assisti()).isTrue();
		
		serie2.addTemporada(temporada2S2);
		bd.persist(serie2);
		//mesma coisa
		assertThat(series.get(1).assisti()).isFalse();
		
		temporada2S1.addEpisodio(episodio1S1T2);
		bd.persist(serie1);
		//agora que um novo episódio foi inserido deve setar pra false
		assertThat(series.get(0).assisti()).isFalse();
		
	}

}