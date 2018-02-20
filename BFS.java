package puzzle;
/* The input is assumed to be in the hexadecimal format i.e. making the goal state = "123456789BCDEF0"*/
import java.io.*;
import java.util.*;

public class puz 
{
	
	public class node
	{
		String state;
		node parent;
	
		public node(String s)
		{
			this.state=s;
			this.parent=null;
		}
	}
	
	Queue <node> q = new LinkedList<node>();
	
	//for initial input
	String d;
	node f=new node(d);
	
	// to keep track of repeated states.
	HashMap<String,Integer> map = new HashMap<String,Integer>();
	long starttime,endtime;// to calculate the running time.
	
	public static void main(String args[]) throws Exception
	{
		puz obj= new puz();
		obj.starttime=System.nanoTime();
		BufferedReader input = new BufferedReader (new InputStreamReader (System.in));
		System.out.println("enter string");
		try
			{
				obj.d = input.readLine();
			}catch(Exception e)
				{System.out.println("IO exception");}
		Runtime runtime = Runtime.getRuntime();
		obj.f.state =obj.d;
		obj.add(obj.f,0);
		// Breadth First Search Logic
		while (obj.q.peek() != null) 
			{
				node c= obj.q.peek();
				if(c.state.equalsIgnoreCase("123456789ABCDEF0")) //checking for goal state.
					{
						obj.print(c);
						obj.endtime = System.nanoTime();
						System.out.println(obj.endtime-obj.starttime + " Nanoseconds is the run time");//total runtime of the program.
						System.out.println((runtime.totalMemory()-runtime.freeMemory())/1024 + " Bytes is the memory used");
						/*Each node will be of the size(string 32bytes, */
						System.exit(0);
					}
				// Move the blank space up
				obj.upPuzzle(obj.q.peek());
				// Move the blank space down
				obj.downPuzzle(obj.q.peek());
				// Move left
				obj.leftPuzzle(obj.q.peek());
				// Move right and remove the head
				obj.rightPuzzle(obj.q.remove());
			}
		System.out.println("No solution for the given Puzzle 15 problem");	
		obj.endtime = System.nanoTime();
		System.out.println(obj.endtime-obj.starttime + " Nanoseconds");//total runtime of the program.
		System.out.println((runtime.totalMemory()-runtime.freeMemory())/1024 + " bytes is the memory used");
	}	
	
	
	public void add(node s, int n) 
	{	
		if (!map.containsKey(s.state)) // check for explored states
			{
				map.put(s.state, n);//adding new states to the hashmap
				q.add(s);// adding new states to the queue
			}
	}
	
	void upPuzzle(node stat) 
	{
		String s = "";
		String b=stat.state;
		int a = b.indexOf("0");
		if (a > 3) 
			{
				s = b.substring(0, a - 4) + "0" + b.substring(a - 3, a)+ b.charAt(a -4) + b.substring(a + 1); //upshift performed.
				node n = new node(s);
				n.parent=stat;// for backtracking the path.
				add(n, map.get(b) + 1);
			}
		}

	void downPuzzle(node stat) 
		{
			String b=stat.state;
			String s = "";
			int a = b.indexOf("0");
			if (a < 12) 
				{
					s = b.substring(0, a) + b.substring(a + 4, a + 5)+ b.substring(a + 1,a + 4) + "0"+ b.substring(a + 5);//downshift performed.
					node n = new node(s);
					n.parent=stat;// for backtracking the path.
					add(n, map.get(b) + 1);
				}
		}

	void leftPuzzle(node stat) 
	{	
		String b= stat.state;
		String s = "";
		int a = b.indexOf("0");
		if (a != 0 && a != 4 && a != 8 && a != 12)
			{
				s = b.substring(0, a - 1) + "0" + b.substring(a-1,a)+ b.substring(a + 1);//left shift performed.
				node n = new node(s);
				n.parent=stat;// for backtracking the path.
				add(n, map.get(b) + 1);
			}
	}

	void rightPuzzle(node stat) 
	{	
		String b= stat.state;
		String s = "";
		int a = b.indexOf("0");
		if (a != 3 && a != 7 && a != 11 && a != 15) 
			{
					if(a==14)
						s = b.substring(0, a) + b.substring(a + 1) + "0";//right shift.
					else
						s = b.substring(0, a) + b.substring(a + 1,a+2) + "0"+ b.substring(a+2);//right shift
				node n = new node(s);
				n.parent=stat;// for backtracking the path.
				add(n, map.get(b) + 1);
			}
	}
	int print(node n)
	{
		if(n.parent!=null)
			print(n.parent); //recursive call to backtrack the path to parent
		else
			{
				System.out.println(n.state);
				return 0;
			}
		System.out.println(n.state);
		return 0;
	}
}