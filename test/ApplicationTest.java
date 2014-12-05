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

public class ApplicationTest {

    public Result resultado;
    public EntityManager em;
    public BD dao;

    @Before
    public void setUp(){
        FakeApplication app = Helpers.fakeApplication(new GlobalSettings());
        Helpers.start(app);
        
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        
        em = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(em);
        em.getTransaction().begin();
        
        dao = new BD();
    }
    
    

    @After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
    }
    
    

    @Test
    public void deveIniciarSerieVazia(){
    	Serie serie = new Serie("Revenge");
    	assertThat(serie.getTemporadasTotal()).isEqualTo(0);
        
    }
    
    @Test
    public void deveAdicionarTemporadas(){
    	Serie serie = new Serie("Revenge");
    	serie.addTemporada(new Temporada(1, serie));
    	assertThat(serie.getTemporadasTotal()).isEqualTo(1);
    }
    
    @Test
    public void deveIniciarTemporadaVazia(){
    	Temporada temporada = new Temporada(1, null);
    	assertThat(temporada.getEpisodios().size()).isEqualTo(0);
    }
    
    @Test
    public void deveAdicionarEpisodioATemporada(){
    	Temporada temporada = new Temporada(1, null);
    	temporada.addEpisodio(new Episodio("pilot", temporada, 1));
    	assertThat(temporada.getNumEpisodios()).isEqualTo(1);
    }
    
    @Test
    public void deveSetarComoNaoAssistido(){
    	Serie serie = new Serie("Revenge");
    	Temporada temporada = new Temporada(1, serie);
    	Episodio episodio = new Episodio("pilot", temporada, 1);
    	temporada.addEpisodio(episodio);
    	serie.addTemporada(temporada);
    	
    	assertThat(serie.assistindo()).isEqualTo(false);
    }
    
    @Test
    public void deveSetarParaAssistido(){
    	Serie serie = new Serie("Revenge");
    	Temporada temporada = new Temporada(1, serie);
    	Episodio episodio = new Episodio("pilot", temporada, 1);
    	temporada.addEpisodio(episodio);
    	serie.addTemporada(temporada);
    	
    	serie.setAssistida(true);
    	
    	assertThat(serie.assistindo()).isEqualTo(true);
    }
    
    @Test
    public void deveInformarProximoEpisodio(){
    	Temporada temporada = new Temporada(1, null);
    	Episodio episodio = new Episodio("pilot", temporada, 1);
    	Episodio episodio2 = new Episodio("outro", temporada, 2);
    	temporada.addEpisodio(episodio);
    	temporada.addEpisodio(episodio2);
    	temporada.getEpisodios().get(0).setAssistido(true);
    	Serie serie = new Serie("Revenge");
    	serie.addTemporada(temporada);
    	
    	assertThat(serie.getProximoEpisodio()).isEqualTo(episodio2);
    }

    /*@Test
    public void deveIniciarSemMetas(){
        resultado = callAction(controllers.routes.ref.Application.metas(),fakeRequest());
        assertThat(contentAsString(resultado)).contains("Você ainda não adicionou metas");
        assertThat(status(resultado)).isEqualTo(Http.Status.OK);
    }
    
    

    @Test
    public void deveAdicionarMetas(){
    	//Cria formulario falso
        Map<String, String> fakeForm = new HashMap<String, String>();
        fakeForm.put("nome", "Lab 2 de SI");
        fakeForm.put("descricao", "Laboratório usando play! e Java");
        fakeForm.put("prioridade", "alta");
        fakeForm.put("ano", "2014");
        fakeForm.put("mes", "12");
        fakeForm.put("dia", "05");
        
        //Chama a acao addMeta() usando o formulario falso
        resultado = callAction(controllers.routes.ref.Application.addMeta(),
                            fakeRequest().withFormUrlEncodedBody(fakeForm));

        assertThat(status(resultado)).isEqualTo(Http.Status.SEE_OTHER);
        assertThat(redirectLocation(resultado)).isEqualTo("/metas");
        
        //Verifica se a meta foi adicionada ao BD
        List<Meta> metas = dao.findAllByClass(Meta.class);
        assertThat(metas.size()).isEqualTo(1);
        assertThat(metas.get(0).getNome()).isEqualTo("Lab 2 de SI");
        List<Meta> result2 = dao.findByAttributeName("Meta",
                "nome", "Lab 2 de SI");
        assertThat(result2.size()).isEqualTo(1);
        

        resultado = callAction(controllers.routes.ref.Application.metas(),
                fakeRequest());
        assertThat(status(resultado)).isEqualTo(Http.Status.OK);
        assertThat(contentAsString(resultado)).contains("Lab 2 de SI");
    }
    
    @Test
    public void deveMostrarMetaComoAlcancada(){
    	//Cria formulario falso
    	Map<String, String> fakeForm = new HashMap<String, String>();
        fakeForm.put("nome", "Lab 2 de SI");
        fakeForm.put("descricao", "Laboratório usando play! e Java");
        fakeForm.put("prioridade", "alta");
        fakeForm.put("ano", "2014");
        fakeForm.put("mes", "12");
        fakeForm.put("dia", "05");
        
        //Chama a acao addMeta() usando o formulario falso
        callAction(controllers.routes.ref.Application.addMeta(),
                   fakeRequest().withFormUrlEncodedBody(fakeForm));
        
        //Configura uma meta como alcançada e atualiza o valor da variavel local
        Meta metaDesejada = (Meta) dao.findAllByClass(Meta.class).get(0);
        Long idMeta = metaDesejada.getId();
        callAction(controllers.routes.ref.Application.setAlcancada(idMeta));
        dao.refresh(metaDesejada);        
        
        //Verifica se a meta desejada foi configurada como alcançada no BD
        assertThat(metaDesejada.getAlcancada()).isTrue();
    }
    
    @Test
    public void deveRemoverMetas(){
    	//Cria formulario falso
    	Map<String, String> fakeForm = new HashMap<String, String>();
        fakeForm.put("nome", "Lab 2 de SI");
        fakeForm.put("descricao", "Laboratório usando play! e Java");
        fakeForm.put("prioridade", "alta");
        fakeForm.put("ano", "2014");
        fakeForm.put("mes", "12");
        fakeForm.put("dia", "05");

        
        //Chama a acao addMeta() usando o formulario falso
        callAction(controllers.routes.ref.Application.addMeta(),
                   fakeRequest().withFormUrlEncodedBody(fakeForm));
        
        //Remove a meta criada pelo formulario falso
        Meta desejada = (Meta) dao.findByAttributeName("Meta", "nome", "Lab 2 de SI").get(0);
        Long idMeta = desejada.getId();
        resultado = callAction(controllers.routes.ref.Application.deleteMeta(idMeta, 0));
        
        //Verifica se a meta foi removida do BD
        List<Meta> metas = dao.findAllByClass(Meta.class);
        assertThat(metas.size()).isEqualTo(0);
        
        //Verifica se o redirecionamento foi realizado com sucesso
        assertThat(status(resultado)).isEqualTo(Http.Status.SEE_OTHER);
        assertThat(redirectLocation(resultado)).isEqualTo("/metas");
        
        //Verifica se a meta foi removida da view
        resultado = callAction(controllers.routes.ref.Application.metas(),
        					fakeRequest());
        assertThat(status(resultado)).isEqualTo(Http.Status.OK);
        assertThat(contentAsString(resultado)).contains("Você ainda não adicionou metas");
        assertThat(contentAsString(resultado)).doesNotContain("Lab 2 de SI");     
    }  
	*/
}
