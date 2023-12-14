package solver;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    int width, height;
    char[][] mapData, itemsData;
    int[] playerPos;
    char parentMove;
    Node parentNode;
    //ArrayList<int[]> boxPos, goalPos, holes;
    int[] boxArr, goalArr;
    int[] boxPlayerArr;
    int boxes, goals;
    int depth;
int potato;
    public Node(int width, int height, char[][] mapData, char[][] itemsData, int depth){
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.mapData = mapData;
        this.itemsData = itemsData;

        this.boxArr = new int[50];
        this.boxes = 0;
        this.goalArr = new int[50];
        this.goals = 0;
        this.boxPlayerArr = new int[50];

        //holes = new ArrayList<>();
        //boxPos = new ArrayList<>();
        //goalPos = new ArrayList<>();
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(itemsData[i][j]=='@'){
                    playerPos = new int[]{i,j};
                    boxPlayerArr[0] = i;
                    boxPlayerArr[1] = j;
                }

                if(itemsData[i][j]=='$'){
                    int[] temp = {i,j};
                    //boxPos.add(temp);
                    boxArr[boxes*2] = i;
                    boxArr[boxes*2+1] = j;

                    boxPlayerArr[boxes*2+2] = i;
                    boxPlayerArr[boxes*2+3] = j;

                    boxes++;
                }
                if(mapData[i][j]=='.'){
                    int[] temp = {i,j};
                    //goalPos.add(temp);
                    goalArr[goals*2] = i;
                    goalArr[goals*2+1] = j;
                    goals++;
                }
//                if(j+2<width){
//                    if(mapData[i][j]=='#' && mapData[i][j+1]==' ' && mapData[i][j+2]=='#'){
//                        int[] temp = {i,j+1};
//                        holes.add(temp);
//                    }
//                }
//                if(i+2<height){
//                    if(mapData[i][j]=='#' && mapData[i+1][j]==' ' && mapData[i+2][j]=='#'){
//                        int[] temp = {i+1,j};
//                        holes.add(temp);
//                    }
//                }
            }
        }
    }

    public boolean checkGoalFound(){
        return Arrays.equals(boxArr, goalArr);
    }

    public void setParent(char move, Node node){
        this.parentMove = move;
        this.parentNode = node;
    }

    public int getHeuristics(){
        int minDist, h=0;
        int[] goalTemp = goalArr.clone();
        for(int i=0; i<boxes; i++){
            minDist = 99999;
            for(int j=0; j<goals; j++){
                int dist = getDistance(boxArr[i*2+1], goalTemp[j*2+1], boxArr[i*2], goalTemp[j*2]);
                minDist = Math.min(minDist, dist);
            }
            h+=minDist;
            h += getDistance(playerPos[1], boxArr[i*2+1], playerPos[0], boxArr[i*2]);
        }
        return h+depth;
    }

//    public int geatHeuristics(){
////        int minimum=9999;
////        ArrayList<int[]> boxPostTemp = (ArrayList<int[]>)boxPos.clone();
////        for(int i=0; i<boxes; i++){
//            ArrayList<int[]> goalPostTemp = (ArrayList<int[]>)goalPos.clone();
//            int minDist;
//            int h = 0;
//            for(int[] boxes : boxPos){
//                int[] hold = new int[2];
//                minDist = 999;
//                for(int[] goals : goalPostTemp){
//                    int dist = getDistance(boxes[1], goals[1], boxes[0], goals[0]);
//                    if(dist<minDist) {
//                        minDist = dist;
//                        hold = goals;
//                    }
//                }
//                //System.out.println(minDist + "->" + boxes[0] + " " + boxes[1] + " " + hold[0] + " " + hold[1]);
//                int[] holeMin = checkHoles(boxes);
//                int boxToHoleToGoalDist = getDistance(boxes[0], holeMin[0], boxes[1], holeMin[1]) + getDistance(holeMin[0], hold[0], holeMin[1], hold[1]);
//                //h+=minDist;
//                for(int[] b : boxPos)
//                    h += getDistance(playerPos[0], b[0], playerPos[1], b[1]);
//
//                h += Math.min(boxToHoleToGoalDist, minDist);
//                goalPostTemp.remove(hold);
//            }
////            int[] temp = boxPostTemp.remove(boxPostTemp.size()-1);
////            boxPostTemp.add(0, temp);
////            minimum = Math.min(h,minimum);
////        }
////        return minimum+depth;
//        //System.out.println(h);
//        return h+depth;
//    }

//    private int[] checkHoles(int[] box){
//        int minHoleDist = 999;
//        int[] hold = new int[2];
//        for(int[] h : holes){
//            int tempDist = getDistance(box[0], h[0], box[1], h[1]);
//            if(tempDist<minHoleDist)
//                hold = h;
//        }
//        return hold;
//    }

    public int getDistance(int x1, int x2, int y1, int y2){
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }
    public char[][] copyItemData(){
        char[][] copy = new char[height][];
        for(int i=0; i<height; i++){
            copy[i] = new char[width];
            System.arraycopy(itemsData[i], 0, copy[i], 0, width);
        }
        return copy;
    }

    public void setDepth(int d){
        this.depth = d;
    }

    public ArrayList<Character> checkMoves(){
        ArrayList<Character> list = new ArrayList<>();
        list.add('u');
        list.add('d');
        list.add('l');
        list.add('r');

        if(mapData[playerPos[0]][playerPos[1]+1]=='#' ||
                (itemsData[playerPos[0]][playerPos[1]+1]=='$' &&
                        (mapData[playerPos[0]][playerPos[1]+2]=='#' || itemsData[playerPos[0]][playerPos[1]+2]=='$')))
            list.remove(Character.valueOf('r'));

        if(mapData[playerPos[0]][playerPos[1]-1]=='#' ||
                (itemsData[playerPos[0]][playerPos[1]-1]=='$' &&
                        (mapData[playerPos[0]][playerPos[1]-2]=='#' || itemsData[playerPos[0]][playerPos[1]-2]=='$')))
            list.remove(Character.valueOf('l'));

        if(mapData[playerPos[0]+1][playerPos[1]]=='#' ||
                (itemsData[playerPos[0]+1][playerPos[1]]=='$' &&
                        (mapData[playerPos[0]+2][playerPos[1]]=='#' || itemsData[playerPos[0]+2][playerPos[1]]=='$')))
            list.remove(Character.valueOf('d'));

        if(mapData[playerPos[0]-1][playerPos[1]]=='#' ||
                (itemsData[playerPos[0]-1][playerPos[1]]=='$' &&
                        (mapData[playerPos[0]-2][playerPos[1]]=='#'|| itemsData[playerPos[0]-2][playerPos[1]]=='$')))
            list.remove(Character.valueOf('u'));

        return cornerDeadlock(list);
        //return list;
    }

    private ArrayList<Character> cornerDeadlock(ArrayList<Character> moves){

        if(moves.contains('r'))
            if(itemsData[playerPos[0]][playerPos[1]+1]=='$')
                if(mapData[playerPos[0]][playerPos[1]+2]!='.')
                    if(mapData[playerPos[0]][playerPos[1]+3] == '#'){
                        if(mapData[playerPos[0]+1][playerPos[1]+2]=='#' || mapData[playerPos[0]-1][playerPos[1]+2]=='#')
                            moves.remove(Character.valueOf('r'));
                    }
//                    else if(itemsData[playerPos[0]][playerPos[1]+3] == '$')
//                        if(itemsData[playerPos[0]+1][playerPos[1]+2]=='$' || itemsData[playerPos[0]-1][playerPos[1]+2]=='$')
//                            moves.remove(Character.valueOf('r'));


        if(moves.contains('l'))
            if(itemsData[playerPos[0]][playerPos[1]-1]=='$')
                if(mapData[playerPos[0]][playerPos[1]-2]!='.')
                    if(mapData[playerPos[0]][playerPos[1]-3] == '#'){
                        if(mapData[playerPos[0]+1][playerPos[1]-2]=='#' || mapData[playerPos[0]-1][playerPos[1]-2]=='#')
                            moves.remove(Character.valueOf('l'));
                    }
//                    else if(itemsData[playerPos[0]][playerPos[1]-3] == '$')
//                        if(itemsData[playerPos[0]+1][playerPos[1]-2]=='$' || itemsData[playerPos[0]-1][playerPos[1]-2]=='$')
//                            moves.remove(Character.valueOf('l'));

        if(moves.contains('d'))
            if(itemsData[playerPos[0]+1][playerPos[1]]=='$')
                if(mapData[playerPos[0]+2][playerPos[1]]!='.')
                    if(mapData[playerPos[0]+3][playerPos[1]] == '#'){
                        if(mapData[playerPos[0]+2][playerPos[1]+1]=='#' || mapData[playerPos[0]+2][playerPos[1]-1]=='#')
                            moves.remove(Character.valueOf('d'));
                    }
//                else if(itemsData[playerPos[0]+3][playerPos[1]] == '$')
//                        if(itemsData[playerPos[0]+2][playerPos[1]+1]=='$' || itemsData[playerPos[0]+2][playerPos[1]-1]=='$')
//                            moves.remove(Character.valueOf('d'));

        if(moves.contains('u'))
            if(itemsData[playerPos[0]-1][playerPos[1]]=='$')
                if(mapData[playerPos[0]-2][playerPos[1]]!='.')
                    if(mapData[playerPos[0]-3][playerPos[1]] == '#'){
                        if(mapData[playerPos[0]-2][playerPos[1]+1]=='#' || mapData[playerPos[0]-2][playerPos[1]-1]=='#')
                            moves.remove(Character.valueOf('u'));
                    }
//                    else if(itemsData[playerPos[0]-3][playerPos[1]] == '$')
//                        if(itemsData[playerPos[0]-2][playerPos[1]+1]=='$' || itemsData[playerPos[0]-2][playerPos[1]-1]=='$')
//                            moves.remove(Character.valueOf('u'));

        return moves;
    }

    public void printMap(){
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

    public void printDetailMap(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(itemsData[i][j]=='$' || itemsData[i][j]=='@')
                    System.out.print(itemsData[i][j]);
                else if(itemsData[i][j]=='$' && mapData[i][j]=='.')
                    System.out.print("*");
                else
                    System.out.print(mapData[i][j]);
            }
            System.out.println();
        }
    }
}
