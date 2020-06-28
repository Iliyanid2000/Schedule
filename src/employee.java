import java.util.*;

public class employee extends Screate
{
ArrayList<ArrayList<Integer>> availability = new ArrayList<>();
int priority;
String name;
int[][][] DaysWorking;
char doubles;
	employee(String name, int priority, char doubles, ArrayList<ArrayList<Integer>> availability)
	{
	this.name = name;
	this.priority = (Integer.toString(priority).matches("[1-3]") ? priority : -1);
	this.doubles = (doubles == 'y' || doubles == 'n') ? doubles : 'n';
	this.availability = availability;
	DaysWorking = new int[7][(this.doubles == 'y') ? 2 : 1][2];//[Day of the Week][Shift of that day][What time hours they're working]
	
	for(int i = 0; i < 7; i++)
		for(int j = 0; j < DaysWorking[i].length; j++)
			for(int k = 0; k < 2; k++)
				DaysWorking[i][j][k] = -1;
	
	
	}

	public String toString()
	{
		String total = "Name: " + name;
		total += "\nAvailable: " + availability.toString();
		total += "\nDoubles: " + doubles;
		total += "\nPriority: " + priority + "\n\n";
		
		return total;		
	}
	
	public boolean addDay(String day,int[] hours)
	{
		try
		{
		int dayWorking = convertDay(day);
		for(int i = 0; i < DaysWorking[dayWorking].length; i++)
			if(DaysWorking[dayWorking][i] != null)
			{
				DaysWorking[dayWorking][i] = hours;
				return true;
			}
		return false;
		}
		catch(Exception e)
		{
			System.err.println("Attempted to add Invalid Day: " + day + " or hours: " + hours[0] + ":" + hours[1]);
			return false;
		}
	}
	
	public boolean removeDay(String day, int[] hours)
	{
		try
		{
			int dayWorking = convertDay(day);
			for(int i = 0; i < DaysWorking[dayWorking].length;i++)
			{
				if(DaysWorking[dayWorking][i] != null)
				{
					if(DaysWorking[dayWorking][i][0] == hours[0] && DaysWorking[dayWorking][i][1] == hours[1])
					{
						DaysWorking[dayWorking][i] = null;
						return true;
					}
				}
			}
				return false;
		}
		catch(Exception e)
		{
			System.err.println("Attempted to remove Invalid Day: " + day + " or hours: " + hours[0] + ":" + hours[1]);
			return false;
		}
	}
	
	public boolean worksDay(String day, int[] hours)
	{
		try
		{
			int dayWorking = convertDay(day);
			for(int i = 0; i < DaysWorking[dayWorking].length;i++)
				if(DaysWorking[dayWorking][i] != null)
					if(DaysWorking[dayWorking][i][0] == hours[0] && DaysWorking[dayWorking][i][1] == hours[1])
						return true;
			
			return false;
		}
		catch(Exception e)
		{
			System.err.println("Attempted to find Invalid Day: " + day + " or hours: " + hours[0] + ":" + hours[1]);
			return false;
		}
	}
	
}

