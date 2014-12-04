package controllers;

import java.util.List;

import models.Episodio;
import models.Serie;
import models.Temporada;
import models.bd.BD;
import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	private static final BD bd = new BD();
	private static Form<Serie> serie = Form.form(Serie.class);
	
	@Transactional
    public static Result index() {		
    	List<Serie> series = bd.findAllByClass(Serie.class);
        return ok(index.render(series));
    }
	
	@Transactional
	public static Result acompanharSerie() {
		Form<Serie> filledForm = serie.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = bd.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Serie serie = bd.findByEntityId(Serie.class, id);
			serie.serieAssistida();
			
			bd.merge(serie);
			bd.flush();
			
			Logger.debug("Assistindo a serie: " + filledForm.data().toString() + " como " + serie.getNome() + " ID: "+serie.getId());
            
			return redirect(routes.Application.index());
		}
	}
	
	@Transactional
	public static Result assistirAEpisodio() {
		Form<Serie> filledForm = serie.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = bd.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Episodio episodio = bd.findByEntityId(Episodio.class, id);
			
			episodio.episodioAssistido();
			
			bd.merge(episodio);
			bd.flush();
			
			Logger.debug("Assistiu a episodio: " + filledForm.data().toString() + " como " + episodio.getNome() + " ID: "+episodio.getId());
            
			return redirect(routes.Application.index());
		}
	}
}
