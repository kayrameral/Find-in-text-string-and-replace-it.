import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FindReplace{

	public static void main(String[] args){

		if(args.length != 8 && args.length != 6){
			System.out.println("Wrong command line arguments!");
			System.exit(0);
		}

		String[] parsedCommand = parseCommandLineArguments(args);
		
		String find = getSubStr(parseFind(parsedCommand[1]));
		String inputFileName = parsedCommand[0];
		String outputFileName = (parsedCommand.length==3)?parsedCommand[0]:parsedCommand[3];
		String replace = parsedCommand[2];
		String output = "";
		
		try{
			File fileScan = new File(inputFileName);
			Scanner scanner = new Scanner(fileScan);

			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				line = line.replaceAll(find, replace);
				output += line + "\n";
			}
			scanner.close();

			FileWriter myWriter = new FileWriter(outputFileName);
			myWriter.write(output);
			myWriter.close();
			System.out.println("Replacement is done successfully.");
		}catch(FileNotFoundException e){
			System.out.println(inputFileName + " not found.");
		}catch(IOException e){
			System.out.println("Couldn't write into " + outputFileName + " file.");
		}
	}

	public static String getSubStr(ArrayList<String> findAll){
		
		String substr = "";
	
		for(String find: findAll){

			if(find.equals("\\?"))
				substr += "\\?";
			
			else if(find.equals("\\]"))
				substr += "\\]";
			
			else if(find.equals("\\["))
				substr += "\\[";
			
			else if( (!find.equals("?")) && (!find.equals("[")) )
				substr += find;

			else if(find.equals("?"))
				substr += ".";
			
			else if(find.equals("["))
				substr += "[";
			
			else if(find.equals("]"))
				substr += "]";
			
		}
		return substr;
	}

	public static String[] parseCommandLineArguments(String[] args){

		int length = args.length/2;
		String[] parsedCommand = new String[length];


		for(int i=0; i<args.length; i++){
			if(args[i].equals("-i") && i%2==0) parsedCommand[0] = args[i+1];
			else if(args[i].equals("-f") && i%2==0) parsedCommand[1] = args[i+1];
			else if(args[i].equals("-r") && i%2==0) parsedCommand[2] = args[i+1];
			else if(args[i].equals("-o") && i%2==0) parsedCommand[3] = args[i+1];
		}
		return parsedCommand;
	}

	public static ArrayList<String> parseFind(String find){

		ArrayList<String> findAll = new ArrayList<>();
		
		int index = 0;
		boolean bracket = false;

		for(int i=0; i<find.length(); i++){

			if(find.charAt(i)=='\\' && (find.charAt(i+1)=='?' || find.charAt(i+1)=='[' || find.charAt(i+1)==']'))
				continue;	

			if(find.charAt(i)=='?' && i!=0 && find.charAt(i-1)=='\\'){
				findAll.add("\\?");
				index+=2;
			}
			else if(find.charAt(i)=='[' && i!=0 && find.charAt(i-1)=='\\'){
				findAll.add("\\[");
				index+=2;
			}
			else if(find.charAt(i)==']' && i!=0 && find.charAt(i-1)=='\\'){
				findAll.add("\\]");
				index+=2;
			}

			else if(find.charAt(i)=='?' && (i==0 || find.charAt(i-1)!='\\')){
				findAll.add("?");
				index+=2;
			}

			else if(find.charAt(i)=='[' && find.charAt(i-1)!='\\'){
				findAll.add("[");
				index+=2;
				bracket = true;
			}

			else if(find.charAt(i)==']' && find.charAt(i-1)!='\\'){
				findAll.add("]");
				index+=2;
				bracket = false;
			}

			else if((bracket) && find.charAt(i)!=','){
				findAll.add(String.valueOf(find.charAt(i)));
				index+=2;
			}

			else if((bracket) && find.charAt(i)==',') continue;

			else
				if(index==findAll.size()){
					findAll.add(String.valueOf(find.charAt(i)));
				}
				else{
					String temp = findAll.get(index) + String.valueOf(find.charAt(i));
					findAll.set(index,temp);
				}
		}
		return findAll;
	}

}
