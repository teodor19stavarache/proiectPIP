package pck1.Proiect_PIP;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Edit_input_txt {
	 public static void update_value(String filePath, String key, String newValue) throws IOException {
	        Path path = Paths.get(filePath);

	        List<String> lines = Files.readAllLines(path);
	        List<String> updatedLines = new ArrayList<>();

	        for (String line : lines) {

	            if (line.startsWith(key + "=")) {
	                updatedLines.add(key + "=" + newValue);
	            } else {
	                updatedLines.add(line);
	            }
	        }

	        Files.write(path, updatedLines);
	    }
	    
	    /**
	     * Daca numarul copiilor este diferit de valoarea 0, se vor introduce anii acestora
	     * @param filePath
	     * @param updateValue
	     * @throws IOException
	     */
	    public static void update_children(String filePath, List<String> updateValue) throws IOException {
	        Path path = Paths.get(filePath);

	        List<String> lines = Files.readAllLines(path);
	        List<String> updatedLines = new ArrayList<>();

	        int numberOfChildren = 0;

	        // Prima dată citim numărul de copii
	        for (String line : lines) {
	            if (line.startsWith("children=")) {
	                numberOfChildren = Integer.parseInt(line.substring("children=".length()).trim());
	                break;
	            }
	        }
	        
	        if(numberOfChildren == 0) return;
	        
	        /**
	         * Daca numarul copiilor este diferit de numarul varstelor primite, va arunca o exceptie+
	         */
	        if (updateValue.size() != numberOfChildren) {
	            throw new IllegalArgumentException("Numărul de vârste (" + updateValue.size() + 
	                                               ") nu coincide cu numărul de copii (" + numberOfChildren + ")");
	        }

	        for (String line : lines) {
	            if (line.startsWith("children_ages=")) {
	            	updatedLines.add("children_ages=" + String.join(",", updateValue));
	            } else {
	                updatedLines.add(line);
	            }
	        }

	        Files.write(path, updatedLines);
	    }
}
