package studienprojekt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Template {
    
    private String content;
    
    public Template() {
        content = ""; 
    }
    
    public Template load(String file) {
        try {
            this.content = readFile(file);
        } catch (IOException ex) {
            Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this;
    }
    
    public Template save(String file) {
       
        finalizeContent();
        
        File outfile = new File(file);
        FileWriter outfileWriter;
        
        try {
            outfileWriter = new FileWriter(outfile);
            outfileWriter.write(this.content);
            outfileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(OutfileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    public Template replace(String key, String value) {
        
        this.content = this.content.replaceAll("\\{" + key + "\\}", value);
        this.content = this.content.replaceAll("\\{loop:" + key + "\\}", value + "{loop:" + key + "}");
        
        return this;
    }
    
    protected void finalizeContent() {
        // Remove unused placeholders
        this.content = this.content.replaceAll("\\{[a-zA-Z]+\\}", "");
        this.content = this.content.replaceAll("\\{loop:[a-zA-Z]+\\}", "");
    }
    
    protected String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String         line;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }
    
    public Template setContent(String content) {
        this.content = content;
        return this;
    }
    
    public String getContent() {
        return this.content;
    }
}
