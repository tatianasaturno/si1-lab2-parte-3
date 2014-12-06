import models.bd.BD;
import models.Episodio;
import models.Serie;
import models.Temporada;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.GlobalSettings;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import scala.Option;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.redirectLocation;
import static play.test.Helpers.status;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestesSemBD {

    public Result resultado;
    public EntityManager em;
    public BD bd;

    @Test
    public void deveIniciarSerieVazia(){
    	Serie serie = new Serie("Revenge");
    	assertThat(serie.isEmpty()).isEqualTo(true);
        
    }
    
    @Test
    public void deveAdicionarTemporadas(){
    	Serie serie = new Serie("Revenge");
    	serie.addTemporada(new Temporada(1));
    	assertThat(serie.getTemporadasTotal()).isEqualTo(1);
    }
    
    @Test
    public void deveIniciarTemporadaVazia(){
    	Temporada temporada = new Temporada(1);
    	assertThat(temporada.getEpisodios().size()).isEqualTo(0);
    }
    
    @Test
    public void deveAdicionarEpisodioATemporada(){
    	Temporada temporada = new Temporada(1);
    	temporada.addEpisodio(new Episodio("pilot", 1));
    	assertThat(temporada.getNumEpisodios()).isEqualTo(1);
    }
    
    @Test
    public void deveSetarComoNaoAssistido(){
    	Serie serie = new Serie("Revenge");
    	Temporada temporada = new Temporada(1);
    	Episodio episodio = new Episodio("pilot", 1);
    	temporada.addEpisodio(episodio);
    	serie.addTemporada(temporada);
    	
    	assertThat(serie.assistindo()).isEqualTo(false);
    }
    
    @Test
    public void deveSetarParaAssistido(){
    	Serie serie = new Serie("Revenge");
    	Temporada temporada = new Temporada(1);
    	Episodio episodio = new Episodio("pilot", 1);
    	temporada.addEpisodio(episodio);
    	serie.addTemporada(temporada);
    	
    	serie.setAssistida(true);
    	
    	assertThat(serie.assistindo()).isEqualTo(true);
    }
    
    @Test
    public void deveInformarProximoEpisodio(){
    	Temporada temporada = new Temporada(1);
    	Episodio episodio = new Episodio("pilot", 1);
    	Episodio episodio2 = new Episodio("outro", 2);
    	temporada.addEpisodio(episodio);
    	temporada.addEpisodio(episodio2);
    	temporada.getEpisodios().get(0).setAssistido(true);
    	Serie serie = new Serie("Revenge");
    	serie.addTemporada(temporada);
    	
    	assertThat(serie.getProximoEpisodio()).isEqualTo(episodio2);
    }
}