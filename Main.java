/*
Group 5 - Mastermind Project
Authors:  Perez, Luvin
Program description:
Mastermind is a code-breaking game. A player and the computer will compete to defeat the each other. The computer generates a code of three random colors and the user has to guess them to break it.
*/

//java util imports
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Stack;
import java.util.*;

class Main {
  //main function will run the main menu method to access the game
  public static void main(String[] args) {
    mainMenu();
  }

  //Welcome and greeting message. This method ask user for username. Returns the username.
  static String welcomeMessage(){
    Scanner input = new Scanner(System.in);
    System.out.print("Hello! What is your name? ");
    String username = input.nextLine();
    System.out.println("\nHey, " + username + ", Welcome to Mastermind!\n");
    return username;
  }

  //mainMenu method. Gets the username from user. Then user must input the prefered option. The menu and program stops when user selects 4 (quit)
  static void mainMenu(){
    String username = welcomeMessage();
    String menuOption = "";
    Scanner input = new Scanner(System.in);
    while(menuOption != "4") {
      System.out.println("\nPlease select one option from the following menu by enter the OPTION\nand then press ENTER:\n");
      System.out.print("1. See rules\n2. Play Game\n3. See Highest Score\n4. Quit\n\nOption: ");
      menuOption = input.nextLine();
      switch(menuOption) {
          case "1":
              readRules();
              break;
          case "2":
              gameLoopController(username);
              break;
          case "3":
              readScoreboard();
              break;
          case "4":
              System.out.println("\n\n\nThanks for playing Mastermind.\n\nGood bye!");
              System.exit(0);
          default:
              System.out.println("\n\n\nYou must choose a valid option\n\n\n");
      }
    }
  }

  //gameLoopController controls the game and counts how many times the user tries to win. Once the game is over, if the user win, is triggered the score table to be created if it still doesn't exist.
  static void gameLoopController(String username){
    int counter = 0;
    int maxTries = 11; //game will stop before 11th try to print "you lose" message.
    String[][] colorsArray = colorsArrayCreator();
    for(int i = 0; i < maxTries; i++){
      if(i > 9){
        System.out.println("\nYou lose!");
        break;
      }
      System.out.println("\n\n****************Attempt number: " + (counter + 1) + "****************");
      String gameTokens[][] = collectColorsCompVsHuman(colorsArray);
      String output = compareFunction(gameTokens);
      counter++;
      if(output == "win"){
        createScoreboard(username, counter);
        break;
      } 
    }
    System.out.println("\n\nGame Over!\n\n\n\n");
    readScoreboard();
    System.out.println("\n\nMain Menu");
  }

  //collectColorsCompVsHuman collects the information from other functions and then return the result to be tested on compareColorsCompVsHuman
  static String[][] collectColorsCompVsHuman(String[][] colorsArray){
    String[] computerChoice = {colorsArray[1][0],colorsArray[1][1],colorsArray[1][2]};
      //uncomment the next line of code to see how the right answer is.
      //System.out.println("\nThe match colors are: "+computerChoice[0]+ " " + computerChoice[1]+ " " + computerChoice[2]);
    String[] userChoice = collectUserInput();
    return compareColorsCompVsHuman(computerChoice, userChoice);
  }

  //compareColorsCompVsHuman check the colors of the user and creates the collection of tokens to be analyzed. Returns tokenColors collection to be analyzed in other method.
  static String[][] compareColorsCompVsHuman(String[] computerColors, String[] userColors){
    String[][] tokenColors = {{"R ", "W ", "O "}, {" ", " ", " "}};
    int incrementIncidences = 0; //This is used for the checkValues method since the method has different uses along the project
    int maxNumberOfColors = 3;
    int counterOccurrences = 0;
    for(int i = 0; i < maxNumberOfColors; i++){
      boolean colorExists = checkValues(computerColors, userColors[i], incrementIncidences);
      boolean colorRepeated = isRepeatedColor(userColors, userColors[i]);
      boolean equalInOnePoint = equalInOnePoint(maxNumberOfColors, computerColors, userColors, userColors[i]);
      if(colorExists){
        if(colorRepeated){
          if(computerColors[i].equals(userColors[i])){
            tokenColors[1][i] = tokenColors[0][0];
            counterOccurrences++;
          }
          else{
            if(equalInOnePoint){
              tokenColors[1][i] = tokenColors[0][2];
            }
            else{
              if(counterOccurrences > 0){
                tokenColors[1][i] = tokenColors[0][2];
              }
              else{
                tokenColors[1][i] = tokenColors[0][1];
                counterOccurrences++;
              }
            }
          }
        }
        else{
          if(computerColors[i].equals(userColors[i])){
            tokenColors[1][i] = tokenColors[0][0];
          }
          else{
            tokenColors[1][i] = tokenColors[0][1];
          }
        }
    }
    else{
      tokenColors[1][i] = tokenColors[0][2];
    }
  }
  return tokenColors;
  }

  //equalInOnePoint checks if the color to be tested will get one R to then return O
  static boolean equalInOnePoint(int maxNumberOfColors, String[] computerColors, String[] userColors, String userColor){
    boolean equalinPoint = false;
    for(int c = 0; c < maxNumberOfColors; c++){
      if(userColor.equals(computerColors[c])){
        if(computerColors[c].equals(userColors[c])){
          return equalinPoint = true;
        }        
      }
    }
    return equalinPoint;
  }

  //isRepeatedColor checks if a color is repeated in user's input
  static boolean isRepeatedColor(String[] userColors, String userColor){
    boolean condition = false;
    int counter = 0;
    for(int i = 0; i < userColors.length; i++){
      if(userColor.equals(userColors[i])){
        counter++;
      }
    }
    if(counter > 1){
      return condition = true;
    }
      return condition;
  }

  //checkValues checks if an specific user color exists in the computer colors collection
  static boolean checkValues(String[] colorComputer, String colorToCheck, int increasePossibilities){
    int maxNumberOfIncidences = 3 + increasePossibilities;
    boolean check = false;
    for(int i = 0; i < maxNumberOfIncidences; i++){
      if(colorToCheck.equals(colorComputer[i])){
        check = true;
      }
    }
    return check;
  }

  //compareFunction compares if the result is win or lose and it returns a string with the words "win" or "failed";
  static String compareFunction(String[][] gameTokens){
    String output = "";
    String[] winnerTokens = {"R ", "R ", "R "};
    String[] userTokens = {gameTokens[1][0], gameTokens[1][1], gameTokens[1][2]};
    if(Arrays.equals(winnerTokens, userTokens)){
      System.out.println("\n\nYou win!\n\n");
      return output = "win";
    }
    else{
      System.out.println("\n\nNice try. Your output is: " + Arrays.toString(userTokens));
      return output = "failed";
    }
  }

  //CollectUserInput collects user input for colors and return them in an array.
  static String[] collectUserInput(){
    String[] possibleColors = { "r", "b", "w", "y", "g" };
    Scanner input = new Scanner(System.in);
    String[] userColors = {"", "", ""};
    System.out.print("\nTell me your first color: ");
    userColors[0] = input.nextLine();
    userColors[0] = isValidColor(userColors[0], possibleColors);
    System.out.print("\nTell me your second color: ");
    userColors[1] = input.nextLine();
    userColors[1] = isValidColor(userColors[1], possibleColors);
    System.out.print("\nTell me your third color: ");
    userColors[2] = input.nextLine();
    userColors[2] = isValidColor(userColors[2], possibleColors);
    return userColors;
  }

  //isValidColor validates user input for any color selected
  static String isValidColor(String userColor, String[] possibleColors){
    Scanner input = new Scanner(System.in);
    boolean condition = checkValues(possibleColors, userColor, 2);
    while(!condition){
      System.out.println("\nError. Something went wrong with your chosen color. \n\nPlease, enter the color again. Remember, your possible colors are: r, b, w, y, g.");
      System.out.print("\nYour color: ");
      userColor = input.nextLine();
      condition = checkValues(possibleColors, userColor, 2);
    }
    return userColor;
  }

  //colorsArrayCreator creates random colors for the game and return them in an array called "colors".
  static String[][] colorsArrayCreator(){
    String[][] colors = {{ "r", "b", "w", "y", "g" },{" ", " "," "}};
    Object[] nonRepeatedNumbers = numberGenerator();
    for(int i = 0; i < nonRepeatedNumbers.length; i++){
      int colorPosition = (int) nonRepeatedNumbers[i];
      colors[1][i] = colors[0][colorPosition];
    }
    return colors;
  }

  //numberGenerator method generates random numbers and then return them in Object array form
  static Object[] numberGenerator(){
    int position;
    int requiredNumbers = 3;
    int maxNumbers = 5;
    Stack <Integer> number = new Stack <Integer>();
    for (int i = 0; i < requiredNumbers; i++) {
      position = (int)Math.floor(Math.random() * maxNumbers );     
      while (number.contains(position)){
        position = (int)Math.floor(Math.random()*maxNumbers);     
        }
        number.push(position);
    }
    Object[] numbersArray = number.toArray();
    return numbersArray;
  }

  //check if the document exists - if doesn't create it - if exists read and rewrite the specific line
  static void createScoreboard(String username, int position) {
    String fileScoreName="gameScores.txt";
    try {
      File readScoreFile = new File(fileScoreName);
      //if the document does not exists, it will be created and scoreboard will be writen
      if(!readScoreFile.exists()) {
        File textFile = new File(fileScoreName);
        textFile.createNewFile();
        writeOnNewDoc(username, position);
        }
      else {
        //function write on specific line
        replaceTextOnFile(username, position);
      }
    }catch (IOException a) {
      System.out.println("An error occurred.");
    }
  }

  //replaceTextOnFile will read all the lines of the gameScores.txt document and it will select the specific line (position) where the user will be placed according to its attemps to win
  static void replaceTextOnFile(String username, int position){
    String fileScoreName="gameScores.txt";
    try{
      String toReplace = Files.readAllLines(Paths.get(fileScoreName)).get(position+1);
      String winner =  position + ". " + username;
      Path path = Paths.get(fileScoreName);
      Stream <String> lines = Files.lines(path);
      List <String> replaced = lines.map(line -> line.replaceAll(toReplace, winner)).collect(Collectors.toList());
      Files.write(path, replaced);
      lines.close();
    }
    catch(IOException b){
    System.out.println("An error occurred.");
    }
  }

  //writeOnNewDoc will write in the new document(gameScores.txt) once it is created by createScoreboard function.
  static void writeOnNewDoc(String username, int position){
    String fileScoreName="gameScores.txt";
    try{
      FileWriter writeOnFile = new FileWriter(fileScoreName);
      String[] gameScore = gameScoreGenerator();
      gameScore[position + 1] = position + ". " + username;
      for(int i = 0; i < gameScore.length; i++){
        writeOnFile.write(gameScore[i] + "\n");
      }
      writeOnFile.close();
    }
    catch(IOException b){
    System.out.println("An error occurred.");
    }
  }

  //gameScoreGenerator will generate the array for the gameScore.txt file.
  static String[] gameScoreGenerator(){
    String[] scoreboard = {"Mastermind Scoreboard:", "", "1. ", "2. ", "3. ", "4. ", "5. ", "6. ", "7. ", "8. ", "9. ", "10. "};
    return scoreboard;
  }

  //readScoreboard will read the gameScores.txt file and will print the data in the screen according to user requirement once the game is over or the user selects 3 on menu
  static void readScoreboard(){
    String fileScoreName = "gameScores.txt";
    int maxNumberOfLinesInScoreboard = 12;
    try{
      File readScoreFile = new File(fileScoreName);
      Scanner readTxtFile = new Scanner(readScoreFile);
      for(int i = 0; i < maxNumberOfLinesInScoreboard; i++){
        while(readTxtFile.hasNextLine()) {
          String data = readTxtFile.nextLine();
          System.out.println(data);
          }
        }
      readTxtFile.close();
    }
    catch(FileNotFoundException e){
      System.out.println("\n\nThere is no scoreboard yet. Play a game to see a scoreboard.");
    }
  }

  //readRules will print in screen the rules for user once option 1 is selected on the mainMenu method
  static void readRules(){
    String masterRulesName = "rulesDoc.txt";
    try {
      int MaxLinesInRulesText = 34;
      File readRulesDocument = new File(masterRulesName);
      Scanner readRulesFile = new Scanner(readRulesDocument);
      for(int i = 0; i < MaxLinesInRulesText; i++) {
        while(readRulesFile.hasNextLine()) {
          String rulesData = readRulesFile.nextLine();
          System.out.println(rulesData);
        }
      }
      readRulesFile.close();
    }
    catch(FileNotFoundException c) {
      System.out.println("\n\nThere is no rules document. Please, contact admin to receive one.");
    }
  }
}
