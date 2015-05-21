/*
 * 
	Author: Jaime Acevedo
   
	Description: EMS class (External Merge Sort) is a program that reads in a binary file and into a specified buffer size.
	The buffer size is specified in it's constructor. The program reads in data into main memory and uses a hashmap to quickly
	sort until the buffer is full. It will repeat until all of the binary file is read and will output the sorted file to an out
	file. 
	
	Date: 07/24/2014
 
 */

import java.io.*;
import java.util.*;
import java.util.TreeMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * @author Acevedo
 */
public class EMS {
    
    
    int bufferSizeMB;
    int fileSize;
    long fileSizeMB;
    final String readFilePath = "relation.bin";
    HashMap<String,String> map = new HashMap<String,String>();
    ValueComparator bvc =  new ValueComparator(map);
    TreeMap<String,String> sorted_map = new TreeMap<String,String>(bvc);
    TreeMap<String,String> sorted_map2 = new TreeMap<String,String>(bvc);

    
EMS(int bufferSizeMB){
      this.bufferSizeMB = bufferSizeMB;  
    }
    
    double getFileSize() {
        File file = new File(readFilePath);

// Get length of file in bytes
        double fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        double fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        double  fileSizeInMB =  fileSizeInKB / 1024;
        fileSizeMB = (long) fileSizeInMB;
        fileSize = (int) fileSizeInBytes;
        return fileSize;

    }


    void writeToFile() {
        
    int bufferSizeBytes = bufferSizeMB * 1024 * 1024;
    int numOfRecords = (int) Math.ceil(this.getFileSize() / 36);
    int numOfFiles = (int) Math.ceil(this.getFileSize() / bufferSizeBytes);
    int RecordsPerFile = numOfRecords / numOfFiles;
        String file = "relation.bin";
        String mode = "rw";


        long iter = 0;
        long tempIter = iter;
        for(int i=1; i<numOfFiles+1; i++) {
        try {
            RandomAccessFile fp = new RandomAccessFile(file, mode);
            System.out.println("Filling Buffer...");
            while (tempIter < bufferSizeBytes) {

                char tempBuff[] = new char[36];
                fp.seek(iter);
                int count = 0;
                while (count < 35) {
                    tempBuff[count] = (char) fp.read();
                    count++;
                    iter++;
                    tempIter++;
                }
                String record = "";
                String fullRecord = "";
                for (int j = 0; j < tempBuff.length; j++) {
                    String temp = "";
                    temp += tempBuff[j];
                    fullRecord += tempBuff[j];
                    if (temp.matches("[a-zA-Z]")) {
                        record += tempBuff[j];
                    }
                }

               sorted_map.put(record, fullRecord);
                sorted_map2.put(record, fullRecord);

            }
        } catch (IOException io) {
            io.printStackTrace();
        }
   // sorted_map.putAll(map);
    System.out.println("Sorting File "+(i)+"...");

    for(Map.Entry<String,String> entry : sorted_map.entrySet()) {
  String key = entry.getKey();
  String value = entry.getValue();
  BufferedWriter out = null;
try  
{
    FileWriter fstream = new FileWriter("out_"+i+".bin", true); //true tells to append data.
    out = new BufferedWriter(fstream);
    out.write(value);
    out.newLine();
    out.close();
}
catch (IOException e)
{
    System.err.println("Error: " + e.getMessage());
}
            
}
    tempIter =0;
    map.clear();
    sorted_map.clear();
        }
    }
    
    void mergeSortFiles(){
  int i = 0;
  int currentBuffer =0;
  
  System.err.println("Total Files in Merging Process: " + 4);
  System.err.println("Estimated MB of outFinalSorted.bin: " + this.fileSizeMB);

  for(Map.Entry<String,String> entry : sorted_map2.entrySet()) {
  String key = entry.getKey();
  String value = entry.getValue();
  BufferedWriter out = null;
try  
{
    
    File f = new File("outFinalSorted.bin");
    FileWriter fstream = new FileWriter("outFinalSorted"+".bin", true); //true tells to append data.
    out = new BufferedWriter(fstream);
    out.write(value);
    out.newLine();
    out.close();
    
}
catch (IOException e)
{
    System.err.println("Error: " + e.getMessage());
}
    }
    }

    //}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        EMS t = new EMS(5);
        
        t.writeToFile();
        
        t.mergeSortFiles();
        
    }

}
class ValueComparator implements Comparator<String> {

    Map< String, String> base;
    public ValueComparator(Map<String, String> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    @Override
    public int compare(String a, String b) {
        return a.compareToIgnoreCase(b);
                // returning 0 would merge keys
    }
}
