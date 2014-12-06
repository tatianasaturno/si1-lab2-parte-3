package controllers;

import java.util.Collections;
import java.util.List;

import models.Episodio;
import models.Serie;
import models.bd.BD;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	private static final BD bd = new BD();
	private static Form<Serie> formulario = Form.form(Serie.class);
	
	@Transactional
    public static Result index() {		
    	List<Serie> series = bd.findAllByClass(Serie.class);
    	Collections.sort(series);
        return ok(index.render(series));
    }
	
	@Transactional
	public static Result acompanharSerie() {
		Form<Serie> filledForm = formulario.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = bd.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Serie serie = bd.findByEntityId(Serie.class, id);			
			serie.setAssistida(true);
            
			bd.merge(serie);
			bd.flush();
			
			return redirect(routes.Application.index());
		}
	}
	
	@Transactional
	public static Result assistirAEpisodio() {
		Form<Serie> filledForm = formulario.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = bd.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Episodio episodio = bd.findByEntityId(Episodio.class, id);
			
			episodio.setAssistido(true);
			
			bd.merge(episodio);
			bd.flush();
			
			return redirect(routes.Application.index());
		}
	}
}
