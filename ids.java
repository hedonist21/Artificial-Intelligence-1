package puzzle;
/* The input is assumed to be in the hexadecimal format i.e. making the goal state = "123456789BCDEF0"*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;

public class ids 
{
	//for the path.
	Stack <String> q = new Stack<String>();
	
	//for initial input
	String z;
	long time[];
	long starttime;// to calculate the running time.
	public static void main(String args[])
	{
		ids obj = new ids();
		int a=0;
		int i =1;
		obj.starttime = System.nanoTime();
		Runtime runtime = Runtime.getRuntime();
		BufferedReader input = new BufferedReader (new InputStreamReader (System.in));
		System.out.println("enter string");
		try
			{
				obj.z = input.readLine();//initial state.
			}catch(Exception e)
				{System.out.println("IO exception");}
		obj.q.push(obj.z);
		obj.time= new long[200];
		
		//iterative depth search logic. i is a arbitrary value to limit the search.
		while(a!=1 && i<200)
		{
			obj.q=new Stack<String>();
			obj.q.push(obj.z);
			obj.search(obj.z,i);
			obj.time[i-1]= (System.nanoTime() - obj.starttime); 
			if(obj.q.peek().equalsIgnoreCase("123456789ABCDEF0"))
			{
				a=1;
				break;
			}
			i++;
			if (runtime.freeMemory() < (.0001) * runtime.totalMemory()){
		        return;
		      }

		}
		//printing the path.
		if(a==1)
		{
			while(!obj.q.empty())
				System.out.println(obj.q.pop());
		}
		else
			System.out.println("Failure");
		//time and memory usage.
		System.out.println((runtime.totalMemory()-runtime.freeMemory())/1024 + " Bytes is the memory used");
		int v=obj.time.length;
		for(int q=0;q<v&&obj.time[q] != 0;q++)
			System.out.println("Time of "+(q+1)+" iteration = "+obj.time[q]+"nanoseconds");
			
	}
	
	void search(String s,int d)
	{
		if(d<0)
			return;
		else
		{
		if(s.equalsIgnoreCase("123456789ABCDEF0"))
			return;
		int i= upPuzzle(s);//new nodes
		if(i==1)
			{
			search(q.peek(),d-1);//recursive call
			if(q.peek().equalsIgnoreCase("123456789ABCDEF0"))//goal state check
				return;//to break the recursion.
			q.pop();
			}
		i=downPuzzle(s);//new nodes
		if(i==1)
			{
			search(q.peek(),d-1);//recursive call
			if(q.peek().equalsIgnoreCase("123456789ABCDEF0")) // goal state check
				return;
			q.pop();
			}
		i=leftPuzzle(s);//new node
		if(i==1)
			{
			search(q.peek(),d-1);//recursive call
			if(q.peek().equalsIgnoreCase("123456789ABCDEF0"))//goal state check
				return;//to break the recursion
			q.pop();
			}
		i=rightPuzzle(s);//new nodes
		if(i==1)
			{
			search(q.peek(),d-1);//recurvise call
			if(q.peek().equalsIgnoreCase("123456789ABCDEF0"))//goal state check
				return;//to get out of recursion.
			q.pop();
			}
		}
	}
	
	int upPuzzle(String b) //for moving the blank tile up.
	{
		String s = "";
		int a = b.indexOf("0");
		if (a > 3)
		s = b.substring(0, a - 4) + "0" + b.substring(a - 3, a)+ b.charAt(a -4) + b.substring(a + 1); //upshift performed.
		int w=q.search(s);
		if(w!=.1 && !s.equals(""))
			{
			q.push(s);
			return 1;
			}
		else
			return 0;
		}

	int downPuzzle(String b) //for moving the blank tile down
		{
			String s = "";
			int a = b.indexOf("0");
			if (a < 12) 
			s = b.substring(0, a) + b.substring(a + 4, a + 5)+ b.substring(a + 1,a + 4) + "0"+ b.substring(a + 5);//downshift performed.
			int w=q.search(s);
			if(w!=.1 && !s.equals(""))
			{
				q.push(s);
				return 1;
			}
			else
				return 0;
		}

	int leftPuzzle(String b) //for moving the blank tile left
	{	
		String s = "";
		int a = b.indexOf("0");
		if (a != 0 && a != 4 && a != 8 && a != 12)
				s = b.substring(0, a - 1) + "0" + b.substring(a-1,a)+ b.substring(a + 1);//left shift performed.
				int w=q.search(s);
				if(w!=.1 && !s.equals(""))
				{
					q.push(s);
					return 1;
				}
				else
					return 0;
	
	}

	int rightPuzzle(String b) //for moving the blank tile right.
	{	
		String s = "";
		int a = b.indexOf("0");
		if (a != 3 && a != 7 && a != 11 && a != 15)
					if(a==14)
						s = b.substring(0, a) + b.substring(a + 1) + "0";//right shift.
					else
						s = b.substring(0, a) + b.substring(a + 1,a+2) + "0"+ b.substring(a+2);//right shift
					
					int w=q.search(s);
					if(w!=.1 && !s.equals(""))
					{
						q.push(s);
						return 1;
					}
					else
						return 0;
		
	}

}