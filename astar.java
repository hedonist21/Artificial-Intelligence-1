package puzzle;

import java.util.HashMap;
import java.util.Stack;



public class astar 
{
	public class node
	{
		String state;
	
		public node(String s)
		{
			this.state=s;
		}
	}
	//for the path.
	Stack <node> q = new Stack<node>();
	String i;
	node n=new node(i);
	//to check for repeated state.
	HashMap<String,Integer> map = new HashMap<String,Integer>();
	long starttime,endtime;// to calculate the running time.
	
	public static void main(String args[])
	{
		astar obj= new astar();
		obj.starttime=System.nanoTime();
		Runtime runtime = Runtime.getRuntime();
		obj.i="123456789abc0def";
		obj.n.state=obj.i;
		obj.q.push(obj.n);
		//to solve by manhattan distance.
		obj.manstar(obj.q.peek(),0);
		node f= obj.q.peek();
		//check to see if goal state reached
		if(f.state.equalsIgnoreCase("123456789abcdef0"))
		{
			int i=0;
			System.out.println("Solution by manhattan distance");
			while(i<obj.q.size())
			{
				f=obj.q.get(i);
				System.out.println(f.state);
				i++;
			}
		}
		else
		System.out.println("Failed by Manhattan distance heuristic at state- "+f.state);
		System.out.println((runtime.totalMemory()-runtime.freeMemory())/(1024)+"KB");
		obj.endtime=(System.nanoTime() - obj.starttime );
		System.out.println(obj.endtime+"nanoseconds");
		obj.starttime=System.nanoTime();
		//referesing the global variables for next heuristic.
		obj.q= new Stack<node>();
		obj.map= new HashMap<String,Integer>();
		obj.q.push(obj.n);
		//function call for displaced tile heuristic.
		obj.distile(obj.q.peek(),0);
		f= obj.q.peek();
		//check to see if goal state reached
		if(f.state.equalsIgnoreCase("123456789abcdef0"))
		{
			System.out.println("Solution by displaced tiles");
			int i=0;
			while(i<obj.q.size())
			{
				f=obj.q.get(i);
				System.out.println(f.state);
				i++;
			}
		}
		else
		System.out.println("Failed by displaced tiles heuristics at state- "+f.state);
		System.out.println((runtime.totalMemory()-runtime.freeMemory())/(1024)+"KB");
		obj.endtime=(System.nanoTime() - obj.starttime );
		System.out.println(obj.endtime+"nanoseconds");
	}
	//function to find the goal state using manhattan distance heuristic
	void manstar(node n,int cost)
	{
		String goal="123456789abcdef0";
		String c= n.state;
		Runtime runtime = Runtime.getRuntime();
		long mem = (runtime.totalMemory()-runtime.freeMemory())/(1024*1024);
		if((!c.equalsIgnoreCase(goal))&& mem < 100)//goal state and memory sanity check
		{
			int l=0;
			int u2[]=new int[4];
			String u1[]=new String[4];
			int a = c.indexOf("0");
			if(a>3)
			{
				u1[l]= upPuzzle(c);
				u2[l]=manhattan(u1[l++])+cost;//f(n)=h(n)+g(n);
			}
			if(a<12)
			{
				u1[l]= downPuzzle(c);
				u2[l]=manhattan(u1[l++])+cost;
			}
			if (a != 3 && a != 7 && a != 11 && a != 15) 
			{
				u1[l]= rightPuzzle(c);
				u2[l]=manhattan(u1[l++])+cost;
			}
			if (a != 0 && a != 4 && a != 8 && a != 12)
			{
				u1[l]= leftPuzzle(c);
				u2[l]=manhattan(u1[l++])+cost;
			}
			//arranging the child node in order of f(n)
			for (int i = 0; i < l-1; i++)      
			{ 
			       // Last i elements are already in place   
			       for (int j = 0; j < l-i-1; j++) 
			           if (u2[j] > u2[j+1])
			         {
			              int temp = u2[j+1];
			              u2[j+1] = u2[j];
			              u2[j] = temp;
			              String t=u1[j+1];
			              u1[j+1]=u1[j];
			              u1[j]=t;
			         }
			}
			for(int i=0;i<l;i++)
			{
				if(!map.containsKey(u1[i]))//check for repeated states.
				{
					node z= new node(u1[i]);
					q.push(z);//adding to the frontier.
					map.put(u1[i],i);//adding to explored nodes
					manstar(q.peek(),++cost);//recursive call
					node t=q.peek();
					String t1=t.state;
					if(t1.equalsIgnoreCase(goal))// check for goal state.
						return;
					else
						q.pop();//backtracking.
				}
			}
		}
		else
			return;
	}
	
	//function to find the manhattan distance
	int manhattan(String s)
	{
		int count=0;
		char e;
		
		String fs="123456789abcdef0";
		for(int i=0;i<s.length();i++)
		{
			char value= s.charAt(i);
			e=fs.charAt(i);
			
			if(value!=e && value!=0)
			{
				int a=fs.indexOf(value);
				int t1=Math.abs((i/4-a/4));
				int t2=Math.abs((i%4)-(a%4));
				count+= t1+t2;
			}
		}
		return count;
	} 
	
	//function to solve the puzzle with displaced tile heuristic.
	void distile(node n,int cost)
	{
		String goal="123456789abcdef0";
		String c= n.state;
		Runtime runtime = Runtime.getRuntime();
		long mem = (runtime.totalMemory()-runtime.freeMemory())/(1024*1024);
		if((!c.equalsIgnoreCase(goal))&& mem < 200)//goal and memory sanity check
		{
			int l=0;
			int u2[]=new int[4];
			String u1[]=new String[4];
			int a = c.indexOf("0");
			if(a>3)
			{
				u1[l]= upPuzzle(c);
				u2[l]=misplace(u1[l++])+cost;//f(n)=h(n)+g(n)
			}
			if(a<12)
			{
				u1[l]= downPuzzle(c);
				u2[l]=misplace(u1[l++])+cost;//f(n)=h(n)+g(n)
			}
			if (a != 3 && a != 7 && a != 11 && a != 15) 
			{
				u1[l]= rightPuzzle(c);
				u2[l]=misplace(u1[l++])+cost;//f(n)=h(n)+g(n)
			}
			if (a != 0 && a != 4 && a != 8 && a != 12)
			{
				u1[l]= leftPuzzle(c);
				u2[l]=misplace(u1[l++])+cost;//f(n)=h(n)+g(n)
			}
			//sorting for f(n) value
			for (int i = 0; i < l-1; i++)      
			{ 
			       // Last i elements are already in place   
			       for (int j = 0; j < l-i-1; j++) 
			           if (u2[j] > u2[j+1])
			         {
			              int temp = u2[j+1];
			              u2[j+1] = u2[j];
			              u2[j] = temp;
			              String t=u1[j+1];
			              u1[j+1]=u1[j];
			              u1[j]=t;
			         }
			}
			for(int i=0;i<l;i++)
			{
				if(!map.containsKey(u1[i]))//checking for repeted states
				{
					node z= new node(u1[i]);
					q.push(z);//adding to frontier
					map.put(u1[i],i);//adding to explored nodes
					distile(q.peek(),++cost);//recursive call
					node t=q.peek();
					String t1=t.state;
					if(t1.equalsIgnoreCase(goal))//goal state check
						return;
					else
						q.pop();
				}
			}
		}
		else
			return;
	}
	
	//function to calculate the no of misplaced tiles at a particular state
	int misplace(String s)
	{
		int l=s.length();
		int count=0;
		char c,f;
		String fs="123456789abcdef0";
		for(int i=0;i<l;i++)
		{
			c=s.charAt(i);
			f=fs.charAt(i);
			if(!(c==f))
				{
					count++;
				}
		}
		return count;
	}
	
	String upPuzzle(String b) 
	{
		String s = "";
		int a = b.indexOf("0");
		if (a > 3) 
			{
				s = b.substring(0, a - 4) + "0" + b.substring(a - 3, a)+ b.charAt(a -4) + b.substring(a + 1); //upshift performed.
				return s;
			}
		return s;
		}

	String downPuzzle(String b) 
		{
			String s = "";
			int a = b.indexOf("0");
			if (a < 12) 
				{
					s = b.substring(0, a) + b.substring(a + 4, a + 5)+ b.substring(a + 1,a + 4) + "0"+ b.substring(a + 5);//downshift performed.
					return s;
				}
			return s;
		}

	String leftPuzzle(String b) 
	{	
		String s = "";
		int a = b.indexOf("0");
		if (a != 0 && a != 4 && a != 8 && a != 12)
			{
				s = b.substring(0, a - 1) + "0" + b.substring(a-1,a)+ b.substring(a + 1);//left shift performed.
				return s;
			}
		return s;
	}

	String rightPuzzle(String b) 
	{	
		String s = "";
		int a = b.indexOf("0");
		if (a != 3 && a != 7 && a != 11 && a != 15) 
			{
					if(a==14)
						s = b.substring(0, a) + b.substring(a + 1) + "0";//right shift.
					else
						s = b.substring(0, a) + b.substring(a + 1,a+2) + "0"+ b.substring(a+2);//right shift
					return s;
			}
		return s;
	}
}