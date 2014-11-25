package studienprojekt;

public class Main {
    
    public static void main(String[] args) {
        
        // Construct and initialize the main object
        Mapper mapper = Mapper.getInstance();
        mapper.initialize();
        
        // Run the main algorithm
        mapper.run();
    } 
}
