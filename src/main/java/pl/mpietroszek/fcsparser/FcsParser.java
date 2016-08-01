package pl.mpietroszek.fcsparser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FcsParser {
    
    public static void main(String[] args){
        String file1 = "3215apc 100004.fcs";
        String file2 = "Stratedigm - S1400 - 8 Peaks Beads.fcs";        
        Path path = Paths.get(file2);
        
        try {
            byte[] fileByte = Files.readAllBytes(path);
            
            byte[] textStartByte = Arrays.copyOfRange(fileByte, 10, 18);
            byte[] textEndByte = Arrays.copyOfRange(fileByte, 18, 26);            
            int textStart = parseIntFromString(parseStringFromByte(textStartByte));
            int textEnd = parseIntFromString(parseStringFromByte(textEndByte));            
            byte[] textByte = Arrays.copyOfRange(fileByte, textStart, textEnd);
            
            byte[] dataStartByte = Arrays.copyOfRange(fileByte, 26, 34);
            byte[] dataEndByte = Arrays.copyOfRange(fileByte, 34, 42);
            int dataStart = parseIntFromString(parseStringFromByte(dataStartByte));
            int dataEnd = parseIntFromString(parseStringFromByte(dataEndByte));            
            byte[] dataByte = Arrays.copyOfRange(fileByte, dataStart, dataEnd);
            
            String text = parseStringFromByte(textByte);
            System.out.println(text);
            
            int par = parseIntFromString(getValueFromText(text, "PAR"));
            int tot = parseIntFromString(getValueFromText(text, "TOT"));
            System.out.println("" + par + " / " + tot);
            
            printData(dataByte, par, tot);
            
        } catch (IOException ex) {
            Logger.getLogger(FcsParser.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private static int parseIntFromByte(byte[] number){
        return ByteBuffer.wrap(number).getInt();
    }
    
    private static int parseIntFromString(String number){
        return Integer.parseInt(number.trim());
    }
    
    private static String parseStringFromByte(byte[] string){
        return new String(string);
    }
    
    private static String getValueFromText(String text, String param){
        if(text.contains(param)){
            String tmp = text.split(param)[1];
            tmp = tmp.substring(1);
            int index = tmp.indexOf("\\");
            return tmp.substring(0, index);
        } else
            return null;
    }

    private static void printData(byte[] dataByte, int par, int tot) {
        ByteBuffer buffer = ByteBuffer.wrap(dataByte);
        for(int j = 0; j <= tot; j++){
            System.out.print("[" + j + "] ");
            for(int i = 0; i <= par; i ++){
                System.out.print(" " + buffer.getInt(j * par + i));
            }
            System.out.print("\n");
        }
        
    }

}
