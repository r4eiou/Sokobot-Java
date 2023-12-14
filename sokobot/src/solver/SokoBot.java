package solver;

import java.util.*;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    /*
     * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
     */
    /*
     * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
     * sequence
     * that just moves left and right repeatedly.
     */
    Node currNode = new Node(width, height, mapData, itemsData, 0);
    long startTime = System.nanoTime();

    Node goal = aSearch(currNode);
    //goal.printMap();
    String path = tracePath(currNode, goal);
    long elapsedTime = System.nanoTime() - startTime;
    System.out.println(path);
    System.out.println("Execution Time: " + elapsedTime/1000000000 + "s");
    return path;
  }

  public static class heuristicsComparator implements Comparator<Node>{
      @Override
      public int compare(Node n1, Node n2) {
          int first = n1.getHeuristics();
          int second = n2.getHeuristics();
          if(first > second)
              return 1;
          else if(first < second)
              return -1;
          return 0;
      }
  }
  //ArrayList<Node> openNodesList = new ArrayList<>();
  PriorityQueue<Node> openNodes = new PriorityQueue<>(new heuristicsComparator());
  //ArrayList<Node> closedNodes = new ArrayList<>();
  HashMap<String, Node> closedNodes = new HashMap<>();
  //HashMap<String, Node> openNodesList = new HashMap<>();
public Node aSearch(Node startState){
  Scanner sc = new Scanner(System.in);
  Node curr = startState;
  boolean goalFound = curr.checkGoalFound();
  int i=0;
  if(!goalFound){
    //openNodesList.put(Arrays.toString(curr.boxPlayerArr), curr);
    openNodes.offer(curr);
    while(!goalFound && !openNodes.isEmpty()){
      curr = openNodes.remove();
      String currPos = Arrays.toString(curr.boxPlayerArr);
      //openNodesList.remove(currPos);
      goalFound = curr.checkGoalFound();
      if(!goalFound) {
        ArrayList<Character> listMoves = curr.checkMoves();
        if (!listMoves.isEmpty()) {
          for (char move : listMoves) {
            Node temp = switch (move) {
              case 'u' -> new Node(curr.width, curr.height, curr.mapData, moveUp(curr), curr.depth + 1);
              case 'd' -> new Node(curr.width, curr.height, curr.mapData, moveDown(curr), curr.depth + 1);
              case 'l' -> new Node(curr.width, curr.height, curr.mapData, moveLeft(curr), curr.depth + 1);
              case 'r' -> new Node(curr.width, curr.height, curr.mapData, moveRight(curr), curr.depth + 1);
              default -> null;
            };
            assert temp != null;
            String tempos = Arrays.toString(temp.boxPlayerArr);
            if(!closedNodes.containsKey(tempos)){
              //if(openNodesList.containsKey(tempos)){
                //int tempH = temp.getHeuristics();
//                if(temp.depth<=openNodesList.get(tempos).depth) {
//                  System.out.println("hi");
//                  openNodes.remove(openNodesList.get(tempos));
//                  openNodesList.get(tempos).setDepth(temp.depth);
//                  openNodesList.get(tempos).setParent(move, curr);
//                  openNodes.add(openNodesList.get(tempos));
//                }
//              }else{
                  temp.setParent(move, curr);
                  openNodes.offer(temp);
                  //openNodesList.put(currPos, curr);
              //}
            }
          }
        }
      }
      closedNodes.put(currPos, curr);
      i++;
    }
  }
  System.out.println("Nodes: " + i);
  return curr;
}
//private int checkFrontier(Node potential){
//  boolean exists = false;
//  int i=0;
//  while(i<openNodesList.size() && !exists){
//    if(Arrays.deepEquals(openNodesList.get(i).itemsData, potential.itemsData)){
//    //if(Arrays.equals(openNodesList.get(i).boxArr, potential.boxArr) && Arrays.equals(openNodesList.get(i).playerPos, potential.playerPos)){
//      exists = true;
//    }
//    i++;
//  }
//  return exists ? i-1 : -1;
//}
public String tracePath(Node start, Node current){
  StringBuilder sb = new StringBuilder();
  while(!current.equals(start)){
  //while(!Arrays.deepEquals(current.itemsData, start.itemsData)){
    sb.insert(0, current.parentMove);
    current = current.parentNode;
  }
  return sb.toString();
}
  public char[][] moveDown(Node state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x+1][y]=='$'){
      swap(x+1, y, copy, 'D');
    }
    swap(x, y, copy, 'D');
    return copy;
  }

  public char[][] moveUp(Node state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x-1][y]=='$'){
      swap(x-1, y, copy, 'U');
    }
    swap(x, y, copy, 'U');
    return copy;
  }

  public char[][] moveLeft(Node state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x][y-1]=='$'){
      swap(x, y-1, copy, 'L');
    }
    swap(x, y, copy, 'L');
    return copy;
  }

  public char[][] moveRight(Node state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x][y+1]=='$'){
      swap(x, y+1, copy, 'R');
    }
    swap(x, y, copy, 'R');
    return copy;
  }

  public void swap(int x, int y, char[][] itemsData, char move){
    char temp;
    switch(move){
      case 'D':
        temp = itemsData[x+1][y];
        itemsData[x+1][y] = itemsData[x][y];
        itemsData[x][y] = temp;
        break;
      case 'U':
        temp = itemsData[x-1][y];
        itemsData[x-1][y] = itemsData[x][y];
        itemsData[x][y] = temp;
        break;
      case 'L':
        temp = itemsData[x][y-1];
        itemsData[x][y-1] = itemsData[x][y];
        itemsData[x][y] = temp;
        break;
      case 'R':
        temp = itemsData[x][y+1];
        itemsData[x][y+1] = itemsData[x][y];
        itemsData[x][y] = temp;
    }
  }

  public void printMap(int width, int height, char[][] mapData, char[][] itemsData){
    //System.out.println("width: " + width + "\nheight: " + height);

    for(int i=0; i<height; i++){
      for(int j=0; j<width; j++){
        System.out.print(mapData[i][j]);
      }
      System.out.println();
    }

    for(int i=0; i<height; i++){
      for(int j=0; j<width; j++){
        System.out.print(itemsData[i][j]);
      }
      System.out.println();
    }
  }

}
//int num = curr.getHeuristics();
//if(num>100)
//  System.out.println("Not Admissible: " + num);

//      System.out.println("Iteration: " + i);
//      curr.printDetailMap();

//      System.out.println("Depth: " + curr.depth);
//      System.out.println("Moved From: " + curr.parentMove);
//      System.out.println("Box Pos: " + Arrays.toString(curr.boxArr));
//      System.out.println("Goal Pos: " + Arrays.toString(curr.goalArr));
//      System.out.println("H: " + curr.getHeuristics());
//      System.out.println();
//      System.out.println("Children:");
//      for(Node n : openNodesList){
//        n.printDetailMap();
//        System.out.println("Depth: " + n.depth);
//        System.out.println("Moved From: " + n.parentMove);
//        System.out.println("Box Pos: " + Arrays.toString(n.boxArr));
//        System.out.println("Goal Pos: " + Arrays.toString(n.goalArr));
//        System.out.println("H: " + n.getHeuristics() + "\n");
//      }
//      System.out.println("***************************************************************");
//      System.out.println("***************************************************************");
//      sc.nextLine();