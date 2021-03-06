//Author Iliyan Dimitrov
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

public class Screate 
{
	private static ArrayList<ArrayList<employee>> Schedule = new ArrayList<>();
	private static ArrayList<employee> totalEmployee = new ArrayList<>();
	private static ArrayList<ArrayList<ArrayList<Integer>>> shiftsAvailable = new ArrayList<>();
	private static boolean debug;

	public static void main(String[] args) 
	{
		debug = true;
		String file = "Schedule.txt";
		String finalSchedule = "";
		getData(file);
		createSchedule();
		exportSchedule(finalSchedule);
		JOptionPane.showMessageDialog(null, "Succesfully Finished Creating Schedule");

		
		
		if(debug)
		{
			System.out.print(totalEmployee);

			/*for(int i = 0;i<shiftsAvailable.size();i++)
			{
				System.out.println("Day: " + (i + 1));
				for(int j = 0; j < shiftsAvailable.get(i).size();j++)
				{
					System.out.println("	Shift " + (j + 1));
					for(int k = 0; k < shiftsAvailable.get(i).get(j).size(); k++)
					{
						System.out.println("		Times: " + shiftsAvailable.get(i).get(k).get(k) + "\n\n");
					}
				}
			}*/
			
			for(int i = 0; i < Schedule.size(); i++)
			{
				System.out.println("Scheduled Day: " + i);
				for(int j = 0; j < Schedule.get(i).size();j++)
				{
					//System.out.println(Schedule.get(i).size());
					for(int k = 0; k < Schedule.get(i).get(j).DaysWorking[i].length; k++)
					{

					if(Schedule.get(i).get(j).DaysWorking[i][k][0] != -1 && Schedule.get(i).get(j).DaysWorking[i][k][1] != -1)
					System.out.println("	Name: " + Schedule.get(i).get(j).name + " Times: " + Schedule.get(i).get(j).DaysWorking[i][k][0] + "-" + Schedule.get(i).get(j).DaysWorking[i][k][1]);
					}
					System.out.println();
				}
			}
		}
		
		
		
		
	}
	
	protected static void exportSchedule(String sFile)
	{
		File file = null;
		FileWriter wrtr = null;
		try
		{
		    SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");  
		    Date date = new Date();  
		    sFile +=formatter.format(date) + "_Schedule.txt"; 
			
		System.out.println(sFile);
		file = new File(sFile);
		if(file.createNewFile())
			System.out.println("Created New Schedule File");

		wrtr = new FileWriter(file);
		String fWrite = "";
			for(int i = 0; i < 7; i++)
			{
				String fDay = "";
				fDay += "\r\n";
				fDay += convertDay(i) + ":     ";
				for(int j = 0 ; j < Schedule.get(i).size(); j++)
				{		
					if(fDay.indexOf(Schedule.get(i).get(j).name, fDay.indexOf(Schedule.get(i).get(j).name) + 1) == -1)
						fDay += Schedule.get(i).get(j).name;
					for(int k = 0; k < Schedule.get(i).get(j).DaysWorking[i].length; k++)
					{	
						
						
						String temp = "";
						if(Schedule.get(i).get(j).DaysWorking[i][k][0] != -1)
							temp = "~" + convertTime(Schedule.get(i).get(j).DaysWorking[i][k][0]) + ":" + convertTime(Schedule.get(i).get(j).DaysWorking[i][k][1]) + "     ";
						if(!fDay.contains(temp))
							fDay += temp;
					}
				}
				fWrite += fDay;
			}
		wrtr.write(fWrite);
		wrtr.close();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Schedule export Failed");
			System.err.println("Failed to Write Schedule");
			if(debug)
				e.printStackTrace();
			System.exit(0);
		}
	}
	
	protected static void createSchedule()
	{
		for(int i = 0; i < 7; i++)	
		{
			ArrayList<employee> temp = new ArrayList<>();
			Schedule.add(temp);
		}
		

		
		for(int i = 0; i < shiftsAvailable.size(); i++)							//Gets the day
		{
			for(int j = 0; j < shiftsAvailable.get(i).size();j++)				//Gets the shift
			{
				EmployeeDone:
				for(int k = 0; k < totalEmployee.size(); k++)					//Gets employee
				{	
					if(totalEmployee.get(k).availability.get(i) != null)
					{
						if(debug)
							System.out.println(i + " " + j + " " + k);
						int empTS = totalEmployee.get(k).availability.get(i).get(0);
						int empTE = totalEmployee.get(k).availability.get(i).get(1);
						int shiftTS = shiftsAvailable.get(i).get(j).get(0);
						int shiftTE = shiftsAvailable.get(i).get(j).get(1);
						
						if((empTS <= shiftTS) && (empTE >= shiftTE))
						{	
							boolean employeeIsValid = true;
							for(int l = 0; l < totalEmployee.get(k).DaysWorking[i].length; l++) //Fix should be chekcing below for availability not days working
							{
								int empDS = totalEmployee.get(k).DaysWorking[i][l][0];
								int empDE = totalEmployee.get(k).DaysWorking[i][l][1];
								
								//Begins checking current employee working time for overlaps
								if(empDS != -1 && empDE != -1)
								{
									if((empDS > shiftTS && empDS < shiftTE) || (empDE > shiftTS && empDE < shiftTE))
										employeeIsValid = false;
								}
							}
							
							for(int l = 0; l < totalEmployee.get(k).DaysWorking[i].length; l++)
								if(employeeIsValid && totalEmployee.get(k).DaysWorking[i][l][0] == -1 && totalEmployee.get(k).DaysWorking[i][l][1] == -1)
								{
									//System.out.println("Added " + totalEmployee.get(k).name + " for day " + i);
									totalEmployee.get(k).DaysWorking[i][l][0] = shiftsAvailable.get(i).get(j).get(0);
									totalEmployee.get(k).DaysWorking[i][l][1] = shiftsAvailable.get(i).get(j).get(1);
									Schedule.get(i).add(totalEmployee.get(k));
									//shiftsAvailable.get(i).get(j).clear();
									//System.out.println("Shift emp start: " + totalEmployee.get(k).availability.get(i).get(0) + ": Shift end: " + totalEmployee.get(k).availability.get(i).get(1));
									//System.out.println("Shift start: " +shiftsAvailable.get(i).get(j).get(0) + ": shift end: " + shiftsAvailable.get(i).get(j).get(1));
									//System.out.println("Schedule size for day " + i + ":" + Schedule.get(i).size());
									//j++;
									break EmployeeDone;
								}
							
						}						
					}
				}
			}
			Collections.shuffle(totalEmployee);
		}
		//System.out.println(Schedule);
	}
	
	protected static void getData(String file)
	{
		File f = null;
		Scanner scnr = null;
		Scanner line = null;
		try 
		{
			f = new File(file);
			scnr = new Scanner(f);
		} 
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Error Invalid File Location: " + file);
			System.err.println("Error Invalid File Location: " + file); 
			System.exit(0);
		}

		scnr.nextLine();
		try
		{	
			for(int i = 0; i < 7; i++)									//Loops 7 times to get all 7 days shifts
			{
				ArrayList<ArrayList<Integer>> day = new ArrayList<>(); 	//Stores the arraylist of in times for that day
				line = new Scanner(scnr.nextLine()); 					//Scanner for the current line of the file
				line.next();											//Skips White Space
				while(line.hasNext())
				{					
					String combinedTimes = line.next();					//Gets Available Shift Times in TTam-TTpm format						
					day.add(convertTime(combinedTimes));				//Adds the current hours worked into day				
				}
				shiftsAvailable.add(day);								//Adds day to the shifts available
			}
			scnr.nextLine();
			scnr.nextLine();
			scnr.nextLine();											//Skips two lines in order to pass white space and get to employees
			
			
			
			while(scnr.hasNextLine())
			{
				line = new Scanner(scnr.nextLine());
				if(line.hasNext())
				{
					String nLine = line.next();
					if(!nLine.equals("") || !nLine.matches(" +"))				//Checks if the new Line is blank or only has spaces in it
					{
						String eName = nLine;
						int ePriority = line.nextInt();
						char eDoubles = line.next().charAt(0);
						String cAvailability = line.next();
						ArrayList<Integer> eHoursAvailable = convertTime(cAvailability);
						ArrayList<ArrayList<Integer>> eDaysAvailable = new ArrayList<>();
					
						for(int i = 0; i < 7; i++)
							eDaysAvailable.add(null);
				
						while(line.hasNext())
						{
							String next = line.next();
							if(next.contains("am") || next.contains("pm"))
								eHoursAvailable = convertTime(next);
						
							else
							{ 
								int index = convertDay(next);
								eDaysAvailable.set(index,eHoursAvailable);
							}
						}
						employee temp = new employee(eName,ePriority,eDoubles,eDaysAvailable);
						totalEmployee.add(temp);
					}
				}
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Error Parsing Data Incorrect Formating for File: " + file);
			System.err.println("Error Parsing Data Incorrect Formating for File: " + file);
			if(debug)
				e.printStackTrace();
			scnr.close();
			System.exit(0);
		}
				
		totalEmployee = sortA(totalEmployee);
		scnr.close();
		
	}
	
	protected static ArrayList<Integer> convertTime(String cTime)
	{
		ArrayList<Integer> hours = new ArrayList<>();		//Makes a new ArrayLis for the hours available for that day		
		String[] splitTimes = cTime.split("-");				//Creates an array of the two in time with 
		for(int j = 0; j < splitTimes.length; j++)			//Goes through array list to remove am and pm notation
		{
			if(splitTimes[j].contains("am"))
			{
				if(j == 1)
					hours.add(Integer.valueOf(splitTimes[j].replace("am","")) + 24);			//Uses 24 hour format for am leave as is
				else
					hours.add(Integer.valueOf(splitTimes[j].replace("am","")));
			}
			
			else if(splitTimes[j].contains("pm"))
				hours.add(Integer.valueOf(splitTimes[j].replace("pm", "")) + 12);	//For pm add 12 hours to convert into 24 hour format
		}
		return hours;
	}
	protected static String convertTime(int input)
	{
		if(input <= 12)
			return String.valueOf(input) + "am";
		if(input > 24)
			return String.valueOf(input - 24) + "am";
		return String.valueOf(input - 12) + "pm";
	}
	
	protected static int convertDay(String day)
	{
		int dayWorking = -1;
		switch(day)
		{
		case "M":  dayWorking = 0;
			break;
		case "T":  dayWorking = 1;
			break;
		case "W":  dayWorking = 2;
			break;
		case "TH": dayWorking = 3;
			break;
		case "F":  dayWorking = 4;
			break;
		case "SA": dayWorking = 5;
			break;
		case "SU": dayWorking = 6;
			break;			
		}
		return dayWorking;
	}
	
	protected static String convertDay(int day)
	{
		String dayWorking = null;
		switch(day + 1)
		{
		case 1:  dayWorking = "M";
			break;
		case 2:  dayWorking = "T";
			break;
		case 3:  dayWorking = "W";
			break;
		case 4: dayWorking = "TH";
			break;
		case 5:  dayWorking = "F";
			break;
		case 6: dayWorking = "SA";
			break;
		case 7: dayWorking = "SU";
			break;			
		}
		return dayWorking;
	}
	
	protected static ArrayList<employee> sortA(ArrayList<employee> list) 
	{		
		for (int i = 0; i < list.size()-1; i++) 
			if (list.get(i).priority < list.get(i+1).priority) 
			{
				employee temp = list.get(i+1);
				list.remove(i+1);
				list.add(i, temp);				
			}
		
		return list;		
	}
	
	protected static boolean contains(String[] array, String input)
	{	
		for(int i = 0; i < array.length; i++)
			if(array[i] != null)
				if(array[i].equals(input))
					return true;
		
		return false;
	}
	
}
