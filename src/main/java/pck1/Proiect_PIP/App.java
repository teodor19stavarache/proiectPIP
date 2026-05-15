package pck1.Proiect_PIP;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
    	
//    	try {
//			Edit_input_txt.update_value("src/main/resources/input.txt","city","Iasi");
//			Edit_input_txt.update_value("src/main/resources/input.txt","children","1");
//			
//			List<String> varstaCopii = Arrays.asList("3");
//			Edit_input_txt.update_children("src/main/resources/input.txt",varstaCopii);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	 try {
//             PipelineRunner.run_pipeline();
//         } catch (Exception e) {
//             System.err.println("Eroare în pipeline: " + e.getMessage());
//             e.printStackTrace();
//         }
    	
//    	List<String> tags = Arrays.asList("sal","sal");
//    	Utilizator util = new Utilizator("sal","sal","parola","sal", tags);
//    	
//    	DataBase_Utilizator.adauga_utilizator(util);
//    	
//    	DataBase_Utilizator.verifica_parola(util);
//    	
//    	DataBase_Utilizator.update_nume(util,"nume");
//    	DataBase_Utilizator.update_parola(util,"parola");
//    	DataBase_Utilizator.update_numar_telefon(util,"numar");
//    	List<String> tags_noi = Arrays.asList("tag1","tag2");
//    	DataBase_Utilizator.update_tags(util,tags_noi);
    	
    	Edit_input_txt.update_value("src/main/resources/overpass_input.txt","city","Vaslui");
    	PipelineRunner_atractii.run_pipeline_atractii();
    }
}
