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
	private static final BD dao = new BD();
	private static Form<Serie> seriesForm = Form.form(Serie.class);
	
	@Transactional
    public static Result index() {		
    	List<Serie> series = dao.findAllByClass(Serie.class);
        return ok(index.render(series));
    }
	
	@Transactional
	public static Result acompanharSerie() {
		Form<Serie> filledForm = seriesForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = dao.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Serie serie = dao.findByEntityId(Serie.class, id);			
			serie.serieAssistida();
            
			dao.merge(serie);
			dao.flush();
			
			Logger.debug("Assistindo a serie: " + filledForm.data().toString() + " como " + serie.getNome() + " ID: "+serie.getId());
            
			return redirect(routes.Application.index());
		}
	}
	
	@Transactional
	public static Result assistirAEpisodio() {
		Form<Serie> filledForm = seriesForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = dao.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Episodio episodio = dao.findByEntityId(Episodio.class, id);
			
			episodio.episodioAssistido();
			
			dao.merge(episodio);
			dao.flush();
			
			Logger.debug("Assistiu a episodio: " + filledForm.data().toString() + " como " + episodio.getNome() + " ID: "+episodio.getId());
            
			return redirect(routes.Application.index());
		}
	}
}