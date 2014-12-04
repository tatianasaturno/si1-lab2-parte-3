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

	private static BD dao = new BD();

	@Override
	public void onStart(Application app) {
		Logger.info("Inciando...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				povoaBD();
				dao.flush();
			}
		});
	}

	@Override
	public void onStop(Application app) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Aplicação finalizando...");
			}
		});
	}

	public void povoaBD() throws Exception {
		String csvFile = Play.application().getFile("seriesFinalFile.csv")
				.getAbsolutePath();
		BufferedReader br = null;
		String linha = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			String[] info = linha.split(cvsSplitBy);
			int numTemporada = 1;
			String nomeSerie = "South Park";

			Serie serie = new Serie("South Park");
			Temporada temporada = new Temporada(1);
			Episodio episodio = new Episodio(1, "Cartman Gets an Anal Probe");

			temporada.addEpisodio(episodio);
			serie.addTemporada(temporada);
			linha = br.readLine();

			while ((linha = br.readLine()) != null) {
				info = linha.split(cvsSplitBy);
				if (nomeSerie.equals(info[0])) {
					if (serie.getNumTemporadas() != 0
							&& serie.getUltimaTemporada().getNumTemporada() == Integer
									.parseInt(info[1])) {
						
						episodio = new Episodio(Integer.parseInt(info[2]),
								info[3]);

						serie.getUltimaTemporada().addEpisodio(episodio);
					} else {
						temporada = new Temporada(Integer.parseInt(info[1]));
						episodio = new Episodio(Integer.parseInt(info[2]),
								info[3]);
						temporada.addEpisodio(episodio);
						serie.addTemporada(temporada);
					}

				} else {
					dao.persist(serie);
					serie = new Serie(info[0]);
					temporada = new Temporada(Integer.parseInt(info[1]));
					episodio = new Episodio(Integer.parseInt(info[2]), info[3]);
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
