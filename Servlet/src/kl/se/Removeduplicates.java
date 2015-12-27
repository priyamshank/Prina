package kl.se;

import java.util.Set;


//Duplicate URLS removal

public class Removeduplicates {
	
	boolean uniq;
	
		public int remove(String str,Set<String> new_unique)
		{
			 uniq=new_unique.contains(str);
			
			if(uniq == true)
			{
			return 1;
			}
			else
			{
				return 0;
			}

		}
}