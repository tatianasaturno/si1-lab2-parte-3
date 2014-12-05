import models.Episodio;
import models.Serie;
import models.Temporada;
import models.bd.BD;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Global extends GlobalSettings {

    private static BD bd = new BD();

    @Override
    public void onStart(Application app) {
        Logger.info("Inicializada...");

        JPA.withTransaction(new play.libs.F.Callback0() {
            @Override
            public void invoke() throws Throwable {            	
            	popularBD();
                bd.flush();                
            }});
    }
    
    @Override
    public void onStop(Application app){
    	JPA.withTransaction(new play.libs.F.Callback0() {
            @Override
            public void invoke() throws Throwable {
            	Logger.info("Finalizando...");
            }});    	
    }
    
	public void popularBD() throws Exception{
		 
		String csvFile = Play.application().getFile("seriesFinalFile.csv").getAbsolutePath();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	
		try {
	
			br = new BufferedReader(new FileReader(csvFile));
			String[] info = line.split(cvsSplitBy);
			Serie serie = new Serie("South Park");
			Temporada temporada =  new Temporada(1, serie);
			Episodio episodio = new Episodio("Cartman Gets an Anal Probe", temporada, 1);
			temporada.addEpisodio(episodio);
			serie.addTemporada(temporada);
			line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				info = line.split(cvsSplitBy);
				//Falta tratar o caso em que a coluna de nome do epi e vazio
				if (serie.getNome().equals(info[0])){
					if (serie.getTemporadasTotal()!=0 && serie.getUltimaTemporada().getNumero()==Integer.parseInt(info[1])) {
						if (info.length>=4)
							episodio = new Episodio(info[3], serie.getUltimaTemporada(),Integer.parseInt(info[2]));
						else
							episodio = new Episodio("", serie.getUltimaTemporada(),Integer.parseInt(info[2]));
						serie.getUltimaTemporada().addEpisodio(episodio);
					} else{
						temporada = new Temporada(Integer.parseInt(info[1]),serie);
						if (info.length>=4)
							episodio = new Episodio(info[3], serie.getUltimaTemporada(),Integer.parseInt(info[2]));
						else
							episodio = new Episodio("", serie.getUltimaTemporada(),Integer.parseInt(info[2]));
						temporada.addEpisodio(episodio);
						serie.addTemporada(temporada);
					}
				} else{
					bd.persist(serie);
					serie = new Serie(info[0]);
					temporada = new Temporada(Integer.parseInt(info[1]),serie);
					if (info.length>=4)
						episodio = new Episodio(info[3], temporada,Integer.parseInt(info[2]));
					else
						episodio = new Episodio("", temporada,Integer.parseInt(info[2]));
					temporada.addEpisodio(episodio);
					serie.addTemporada(temporada);
				}
			}
		}
								
	
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}