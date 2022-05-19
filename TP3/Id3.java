import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;


public class Id3 {

   static Scanner in  = new Scanner(System.in);
   static String [][] table = null;
   
    public static void main(String[] args) {
        menu();
        ID3_calculation obj = new ID3_calculation(table);
        obj.calculate_class();
        obj.calculate_attribute();
        obj.calculate_entropy();
        obj.information_gain();
        
        List<Node> node = obj.getNode();
        HashMap<String,Double > information_gain = obj.getInformationGain();
        HashMap<String,String > information_gain_subAttribute = obj.getInformationGain_of_subAttribute();
        
        Vector attributes = obj.getlistofAttributes();
        GenerateTree tree = new GenerateTree(attributes , node , information_gain , information_gain_subAttribute  );
        tree.create_tree();
       tree.Display_attribute();
        tree.display_tree();
        
    }
    
    
    public static void menu(){
    
    String choose="";   
    System.out.println("------- Decision Tree Implementation using ID3 Algorithm -------");
    System.out.println("------- Select one DataSet -------");
    System.out.println("------- Press 1 for Resturant-------");
    System.out.println("------- Press 2 for Weather -------");
    System.out.println("------- Press 3 for Iris -------");
    
    choose = in.nextLine();
    
    switch(choose){
    
        case "1":
            readCSV("restaurant.csv");
            change_numeric_to_name(1);
        break;
        
        case "2":
            readCSV("weather.csv");
            change_numeric_to_name(2);
        break;
        
        case "3":
            readCSV("iris.csv");
            change_numeric_to_name(3);
        break;
        
        default:
        System.out.println("Invalid Choose");
        break;
        
    }
    
    }
    
    
    public static void change_numeric_to_name( int i ){
    
    
        if(i == 1){
         
            for(int j = 1 ; j < table.length; j++){
            
                if(table[j][0].equals("Yes")){
                   table[j][0] = "T";
                }else{
                   table[j][0] = "F";
                }
                
                
                 if(table[j][1].equals("Yes")){
                   table[j][1] = "T";
                }else{
                   table[j][1] = "F";
                }
                
                 
                  if(table[j][2].equals("Yes")){
                   table[j][2] = "T";
                }else{
                   table[j][2] = "F";
                }
                
                  
                  if(table[j][3].equals("Yes")){
                   table[j][3] = "T";
                }else{
                   table[j][3] = "F";
                }
            
                  
                    if(table[j][6].equals("Yes")){
                   table[j][6] = "T";
                }else{
                   table[j][6] = "F";
                }
            
                 
               if(table[j][7].equals("Yes")){
                   table[j][7] = "T";
                }else{
                   table[j][7] = "F";
                }
            
                    
                  
            }
            
        }
        else if(i ==2 ){
        
            for(int j = 1 ; j < table.length; j++){
            
                
                
                    if( Integer.parseInt(table[j][1]) >= 80  ){
                       table[j][1] = "hot";
                    }
                    else if( Integer.parseInt(table[j][1]) >= 70  ){
                       table[j][1] = "mild";
                    }
                    else if( Integer.parseInt(table[j][1]) < 70  ){
                       table[j][1] = "cool";
                    }
                    
                    
                    
                    
                    if( Integer.parseInt(table[j][2]) > 80  ){
                       table[j][2] = "high";
                    }
                    else if( Integer.parseInt(table[j][2]) <= 80  ){
                       table[j][2] = "normal";
                    }
                    
                    
                
            
            }
            
        }
        else{
        
                for(int j = 1 ; j < table.length; j++){
                
                    


                    // sepal lenght
                    if( Double.parseDouble(table[j][0]) > 7  ){
                       table[j][0] = ">7";
                    }
                    
                    else if( Double.parseDouble(table[j][0]) > 6.1  ){
                       table[j][0] = ">6.1";
                    }
                   
                    
                    else if( Double.parseDouble(table[j][0]) > 5.4  ){
                       table[j][0] = ">5.4";
                    }
                   
                    else if( Double.parseDouble(table[j][0]) <= 5.4  ){
                       table[j][0] = "<=5.4";
                    }
                   
                  
                    // sepal width
                     if( Double.parseDouble(table[j][1]) > 3  ){
                       table[j][1] = ">3";
                    }
                    else if( Double.parseDouble(table[j][1]) <= 3  ){
                       table[j][1] = "<=3";
                    }
                    
                    // pedal lenght
                    
                     if( Double.parseDouble(table[j][2]) > 4.9  ){
                       table[j][2] = ">4.9";
                    }
                    else if( Double.parseDouble(table[j][2]) <= 4.9  ){
                       table[j][2] = "<=4.9";
                    }
                    
                     
                     // pedal width
                     
                   if( Double.parseDouble(table[j][3]) > 1.7  ){
                       table[j][3] = ">1.7";
                    }
                   
                   
                   else if( Double.parseDouble(table[j][3]) > 1.5  ){
                       table[j][3] = ">1.5";
                    }
                 
                    
                   else  if( Double.parseDouble(table[j][3]) > 0.6  ){
                       table[j][3] = ">0.6";
                    }
                    else if( Double.parseDouble(table[j][3]) <= 0.6  ){
                       table[j][3] = "<=0.6";
                    }
                    
                    
                }
            
        }
        
        
    }
    
    public static void readCSV(String filename){
    
        String csvFile = filename;
        BufferedReader br = null;
        BufferedReader pre_count = null;
        
        String line = "";
        String cvsSplitBy = ",";
        
        int row=0;
        int col=0;
        

        try {

            pre_count = new BufferedReader(new FileReader(csvFile));
            
            pre_count = new BufferedReader(new FileReader(csvFile));
            while ((line = pre_count.readLine()) != null) {

                // use comma as separator
                String[] attributes = line.split(cvsSplitBy);
                col = attributes.length - 1;
                row++;
            }
            
            // size of table
            
            table  = new String [row][col];
            
            int rows =0;
            
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                
                String[] attributes = line.split(cvsSplitBy);
            
                
                for(int i = 1 ; i < col+1 ; i++){
                    table[rows][i-1] = attributes[i]; 
                 
                }
                
          
                rows++;
                
                

            }

        } 
        catch (IOException e) {
           System.out.println("File not found Exception");
         } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                  System.out.println("File not found Exception Finally");
                }
            }
        }
    
    }
    
}
