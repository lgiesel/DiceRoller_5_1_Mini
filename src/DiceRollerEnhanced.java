import java.text.NumberFormat;
import java.util.Scanner;

public class DiceRollerEnhanced {

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//!!!!!!!!!! DiceRollerEnhanced Enhanced !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// Added common print message method                                   
	// Added method to calculate special roll message using constants  
	// Added prompt to track specific type of roll to track times it rolls
	// Added flag to prompt to track roll or not - defaulted to false; set trackRoll variable to true to prompt for tracking
	// Calculated track roll % and formatted it to print tracking message 
	// Added data validation for track roll type entered 
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	static final boolean PRINTLINE = true;
	final static String  SNAKE_EYES = "Snake Eyes";
	final static String  BOX_CARS = "Box Cars";
	final static String  DOUBLES = "Doubles";
	final static String  LUCKY_7S = "Lucky Sevens";
	final static int     MAXROLLTYPE = 4;
	final static int     MINROLLTYPE = 1;
	
	static int rollCounter = 0;
	static int rollTypeCounter = 0;
	
	static boolean trackRoll = false;	//Set to true and save to prompt user for tracking roll type; default to false so no prompt 

	public static void main(String[] args) {
		
		String roll1 = "Y";
		String choice = "Y";
		int rollType = 0;
    	String trackRollMsg = "";

		final int DIE_ROLL = 6;

		Scanner sc = new Scanner(System.in);
		printMessage("Dice Roller", PRINTLINE);
		
		if (trackRoll) {//To track specific roll type if true; defaulted to false
			printBlankLine();
			String trackPrompt = "Track the number of times a roll type appears.\n" +
								 "(1-Snake Eyes, 2-Box Cars, 3-Doubles, 4-Lucky Seven).\n" +
								 "Enter the number next to roll type to track it:\t";
			rollType = getIntWithinRange(sc, trackPrompt, MINROLLTYPE, MAXROLLTYPE);		
	    	trackRollMsg = getSpecialType(rollType);
		}
				
		while (choice.equalsIgnoreCase("y")) {
			String rollMessage = "";
			
			printBlankLine();
			if (roll1.equalsIgnoreCase("Y")){
				if (!trackRoll) {
					roll1 = askToContinue(sc, "Roll dice? (y/n): ");
				} else {
					roll1 = askToContinue(sc, "Roll dice and track " + trackRollMsg + "? (y/n): ");					
				}
				printBlankLine();
				if (roll1.equalsIgnoreCase("n")){
					//Dont want Roll dice msg to display again
					break;
				} else {//Only want Roll Dice msg to print 1x
					roll1 = "n";
				}
			}
									
			//Roll the dice
			int die1 = getRandomNbr(DIE_ROLL);
			int die2 = getRandomNbr(DIE_ROLL);
			int dice = die1 + die2;
			rollMessage = "Die 1:\t" + die1 + "\nDie 2:\t" + die2 + "\nTotal:\t" + dice;								
			printMessage(rollMessage, PRINTLINE);						 
				
			String specialMsg = getSpecialDiceMessage(dice, die1, die2);
			if (!specialMsg.equals("")){
				printMessage(specialMsg, true);					
			}			

	        if (trackRoll) {//Only call if tracking it on
	        	incrementRollCounters(rollType, die1, die2);	        	
	        }

			printBlankLine();
			choice = askToContinue(sc, "Roll again? (y/n): ");
			if (choice.equalsIgnoreCase("n")){
				break;
			}		
		}

        if (trackRoll) {//Only perform if tracking it on
        	printTrackingMsg(trackRollMsg);
        }

		printBlankLine();
		printMessage("Bye!", PRINTLINE);
	}//Main
	
	private static void printMessage(String inMessage, boolean inLine) {//Print nonblank messages
		if (inLine) {
			System.out.println(inMessage);
		} else {
			System.out.print(inMessage);			
		}				
	}

	private static void printBlankLine() {
		System.out.println();
	}

	public static int getRandomNbr(int inNumber) {
		int nbr = (int) (Math.random() * inNumber) + 1;
		return nbr;
	}

	public static String getSpecialDiceMessage(int inDiceTotal, int inDie1, int inDie2) {
		String specMessage = "";
		
		if (inDiceTotal == 7) {//Most common roll is 7
			specMessage = LUCKY_7S + "!";
		} else if (inDiceTotal == 12) {//Special type of doubles
			specMessage = BOX_CARS + "!";
		} else if (inDiceTotal == 2) {///Special type of doubles 
			specMessage = SNAKE_EYES + "!";
		} else if (inDie1 == inDie2) {
			specMessage = DOUBLES + "!";
		}
		return specMessage;
	}
	
	private static void incrementRollCounters(int inRollType, int inDie1, int inDie2){
		rollCounter++; //Always increment for each roll; denominater for tracking
		
		//Increment roll type counter only when tracking condition met
		if ( (inDie1 + inDie2 == 7 && inRollType == 4)   || // LUCKY7      OR
			 (inDie1 + inDie2 == 12 && inRollType == 2)  || // BOXCARS     OR 
             (inDie1 + inDie2 == 2 && inRollType == 1)   || // SNAKE EYES  OR
			 (inDie1 == inDie2 && inRollType == 3) ) {      // DOUBLES     
		
			rollTypeCounter ++; //numerator for tracking
		}			
	}
	
	private static String getSpecialType(int inRollType) {
		String rollTypeMsg = "";
		if (inRollType == 1) {
			rollTypeMsg = SNAKE_EYES;
		} else if (inRollType == 2) {
			rollTypeMsg = BOX_CARS;
		} else if (inRollType == 3) {
			rollTypeMsg = DOUBLES;
		} else if (inRollType == 4) {
			rollTypeMsg = LUCKY_7S;
		}
		return rollTypeMsg;
	}
	
	public static int getInt(Scanner sc, String prompt) {
		int d = 0;
		boolean isValid = false;
		while (!isValid) {
			System.out.print(prompt);
			if (sc.hasNextInt()) {
				d= sc.nextInt();
				isValid = true;
			} else {
				printBlankLine();
				System.out.print("Error! Invalid number. Try again.\n");				
				printBlankLine();
			}
			sc.nextLine();
		}	
		return d;	
	}
		
	public static int getIntWithinRange(Scanner sc, String prompt,int min, int max) {
		int d = 0;
		boolean isValid = false;
		while (!isValid) {
			d=getInt(sc, prompt);
			if (d<min || d> max) {
				printBlankLine();
				System.out.print("Error! Number must be >= " + min + " and <= " + max + ". Try again.\n");				
				printBlankLine();
			} else {
				isValid = true;
			}
		}
		return d;		
	}
	
	private static void printTrackingMsg(String inTrackMsg) {
		NumberFormat percent = NumberFormat.getPercentInstance();
		percent.setMinimumFractionDigits(1);
		double rollPct = (double) rollTypeCounter / (double) rollCounter;	//Cast int to double for calculation
		 
		printBlankLine();        
		printMessage("Percentage of rolls for " + inTrackMsg + " out of " + rollCounter + " rolls: " + percent.format(rollPct), PRINTLINE);
	}
		
	public static String askToContinue(Scanner inSC, String inMessage) {
		String choice = "";
		boolean line = false;
		
		while (true) {
			printMessage(inMessage, line);
			choice = inSC.next();
			if (choice.equalsIgnoreCase("y")){
				break;
			} else if (choice.equalsIgnoreCase("n")){ 
				break;
			} else if (choice.equals("")){
				line = true;
				printBlankLine();
				printMessage("Error! This entry is required. Try again.\n", line);
			} else {
				line = true;
				printBlankLine();
				printMessage("Error! Entry must be 'y' or 'n'. Try again.\n", line);				
			}
			inSC.nextLine();
		}			
		return choice;
	}	

}//class New
