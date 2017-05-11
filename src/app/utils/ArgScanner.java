package app.utils;

import java.util.HashMap;

public class ArgScanner {
	
	public static HashMap<String, String> getArgList(String[] args){
		
		HashMap<String, String> argList = new HashMap<>();
		
		String lastArg = "";
		
		for (String arg : args){
			
			char[] chaarg = new char[arg.length()];
			arg.getChars(0, arg.length(), chaarg, 0);

			if (arg.length() > 1 && chaarg[0] == '-'){
				argList.put(arg, "");
				if (arg.length() > 2 && chaarg[1] == '-'){
					lastArg = "";
				} else {
					lastArg = arg;
				}
				
			} else {
				argList.put(lastArg, arg);
			}

		}
		
		return argList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
