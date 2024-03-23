package entity;

import util.PermUtil;
import util.PermutationRank;

import java.util.*;

public class Matching {


    private HashMap<Integer, int[]> permutationHashMap = new HashMap<>();


    private int[] pairs;
    private ArrayList<Integer> matchedArrayList;

    private HashMap<Integer, Integer> matchedHashMap;

    private HashMap<Integer, Integer> predecessor = new HashMap<>();
    private ArrayList<Integer> unmatched;
    private final PermUtil permUtil = new PermUtil();
    private final PermutationRank permRank = new PermutationRank();

    /*
        L: Sequence of random index of perm
     */
    private ArrayList<Integer> L;
    private HashSet<Integer> LHashSet;

    public void initUnmatched(Problem problem) {
        unmatched = new ArrayList<>();
        for (int i = 0; i < problem.getnP(); i++) {
            unmatched.add(i);
        }
    }

    public void initL(int N) {
        // L: Store the sequence of random access, DO NOT modify
        L = new ArrayList<>();
        // LHashSet: Only store unmatched
        LHashSet = new HashSet<>();
        for (int i = 0; i < N; i++) {
            L.add(i);
            LHashSet.add(i);
        }
        Collections.shuffle(L);
    }

    public Matching(Problem problem) {
        setPermutationHashMap(problem);
        initPairs(problem);
        matchedArrayList = new ArrayList<>();
        initUnmatched(problem);
    }

    public void initPairs(Problem problem) {
        int N = problem.getnP();
        this.pairs = new int[N];
        for (int i = 0; i < N; i++) {
            pairs[i] = -1;
        }
    }

    public void setPermutationHashMap(Problem problem) {
        int n = problem.getN();
        int N = problem.getnP();
        for (int i = 0; i < N; i++) {
            int[] perm = permUtil.initPerm(n);
            permRank.unrank(n, i, perm);
            this.permutationHashMap.put(i, perm);
        }
    }


    public void findMatchingRandom(Problem problem, Neighbor neighbor) {
        initPairs(problem);
        // L: random sequence to traverse N perms
        initL(problem.getnP());
        int i = 0;
        while (i < L.size()) {
            int curr = L.get(0);
            ArrayList<Integer> currNbs = neighbor.getNeighbors(curr);
            Collections.shuffle(currNbs);
            for (int j = 0; j < currNbs.size(); j++) {
                int nbr = currNbs.get(j);
                if (L.contains(nbr)) {
                    pairs[curr] = nbr;
                    pairs[nbr] = curr;
                    L.removeIf(s -> s.equals(curr));
                    L.removeIf((s -> s.equals(nbr)));
                    break;
                }
                if (j == currNbs.size() - 1) {
                    i++;
                }
            }
        }
        System.out.println(Arrays.toString(pairs));
    }

    public void findMatchingMM1(Problem problem, Neighbor neighbor) {
        initL(problem.getnP());
        while (!unmatched.isEmpty()) {
            Collections.shuffle(unmatched);
            int currNode = unmatched.get(0);
            ArrayList<Integer> neighborsOfCurrNode = neighbor.getNeighbors(currNode);
            Collections.shuffle(neighborsOfCurrNode);
            int nextNode = neighborsOfCurrNode.get(0);
            // Next node is unmatched, just add (currNode, nextNode) to matched and remove them from unmatched
            if (unmatched.contains(nextNode)) {
                matchedArrayList.add(currNode);
                matchedArrayList.add(nextNode);
                unmatched.remove(0);// Remove current node
                final int finalNextNode = nextNode;
                unmatched.removeIf(s -> s.equals(finalNextNode));
            } else {
                // Next node is already matched, do random walk
                ArrayList<Integer> visited = new ArrayList<>();
                boolean deadEnd;
                do {
                    deadEnd = true;
                    visited.add(currNode);
                    if (visited.size() % 2 != 0) {
                        for (int nbr : neighbor.getNeighbors(currNode)) {
                            if (!visited.contains(nbr)) {
                                currNode = nbr;
                                deadEnd = false;
                                break;
                            }
                        }
                    } else {
                        currNode = findPartner(currNode);
                        deadEnd = false;
                    }
//                    System.out.println(visited);
                    if (unmatched.contains(currNode) && visited.size() % 2 == 1) {
                        visited.add(currNode);
                        for (int v : visited) {
                            unmatched.removeIf(s -> s.equals(v));
                            matchedArrayList.removeIf(s -> s.equals(v));
                        }
                        matchedArrayList.addAll(visited);
                        break;
                    }
                } while (!deadEnd);
            }
//            System.out.println(String.valueOf(matched.size())+matched);
            System.out.println(matchedArrayList.size());
        }

    }

    private int findPartner(int nodeIndex) {
        for (int i = 0; i < matchedArrayList.size(); i++) {
            if (matchedArrayList.get(i) == nodeIndex) {
                if (i % 2 == 0) {
                    return matchedArrayList.get(i + 1);
                } else {
                    return matchedArrayList.get(i - 1);
                }
            }
        }
        return -1;
    }

    private int BFS(int currNode, Neighbor neighbor) {
        ArrayList<Integer> visted = new ArrayList<>();
        LinkedList<Integer> linkedListQueue = new LinkedList<>();
        visted.add(currNode);
        linkedListQueue.add(currNode);
        predecessor.put(currNode, -1);
        //
        ArrayList<Integer> deadendList = new ArrayList<>();
        int unmatchedNodeInTheEndOfThePath = 0;
        while (!linkedListQueue.isEmpty()) {
            // Scenario: Find a new pair
            currNode = linkedListQueue.poll();
//            System.out.print(currNode + " ");
            ArrayList<Integer> currNbrs = neighbor.getNeighbors(currNode);
            for (int nbr : currNbrs) {
                if (visted.contains(nbr)) continue;
                if (unmatched.contains(nbr)) {
                    // Good news, finally find an unmatched node
                    linkedListQueue.clear();
                    unmatchedNodeInTheEndOfThePath = nbr;
                    predecessor.put(unmatchedNodeInTheEndOfThePath, currNode);
                    return unmatchedNodeInTheEndOfThePath;
                }
                int partner = findPartner(nbr);
                ArrayList<Integer> partnerNbrs = neighbor.getNeighbors(partner);
                boolean deadendFlag = true;
                for (int it : partnerNbrs) {
                    if (!visted.contains(it)) {
                        deadendFlag = false;
                        break;
                    }
                }
                if (deadendFlag) {
                    deadendList.add(partner);
                }
                if (!visted.contains(nbr) && !deadendList.contains(partner)) {
                    visted.add(nbr);
                    // Add also the partner
                    visted.add(partner);
                    linkedListQueue.add(partner);
                    predecessor.put(nbr, currNode);
                    predecessor.put(partner, nbr);
                }
            }

        }
        return -1;
    }

//    ArrayList<Integer> getRandomWalkPath(int unmatchedNodeInTheEndOfPath) {
//        int curr = unmatchedNodeInTheEndOfPath;
//        ArrayList<Integer> path = new ArrayList<>();
//        while (predecessor.get(curr) != -1) {
//            path.add(curr);
//            curr = predecessor.get(curr);
//        }
//        path.add(curr);
//        return path;
//    }

//    public void findMatchingRandomWalkBfs(Problem problem, Neighbor neighbor) {
//        initL(problem.getnP());
//        matchedArrayList = new ArrayList<>();
//        initUnmatched(problem);
//        for (int i = 0; i < L.size(); ) {
//            int currNode = L.get(0);
//            ArrayList<Integer> currNbrs = neighbor.getNeighbors(currNode);
//            Collections.shuffle(currNbrs);
//            // check if there is unmatched nbr
//            boolean unmatchedNbrExists = false;
//            int designatedNbr = 0;
//            for (Integer nbr : currNbrs) {
//                if (unmatched.contains(nbr)) {
//                    unmatchedNbrExists = true;
//                    designatedNbr = nbr;
//                    break;
//                }
//            }
//            // if unmatchedNbrExists == true, just add to matched and do the remove
//            if (unmatchedNbrExists) {
//                matchedArrayList.add(currNode);
//                matchedArrayList.add(designatedNbr);
//                unmatched.removeIf(s -> s.equals(currNode));
//                int finalDesignatedNbr = designatedNbr;
//                unmatched.removeIf(s -> s.equals(finalDesignatedNbr));
//                L.remove(0);
//                L.removeIf(s -> s.equals(finalDesignatedNbr));
//            } else {// if unmatchedNbrExists == false, do bfs + expand
//                // Expand from currNode alternatively, until:(1)meet an unmatched node(2)dead end
//                int unmatchedNodeInTheEndOfPath = BFS(currNode, neighbor);
//                ArrayList<Integer> randomWalkPath = getRandomWalkPath(unmatchedNodeInTheEndOfPath);
////                System.out.println("randomWalkPath: "+randomWalkPath);
//                for (int j = 0; j < randomWalkPath.size(); j++) {
//                    int finalJ = j;
//                    matchedArrayList.removeIf(s -> s.equals(randomWalkPath.get(finalJ)));
//                }
//                for (int j = 0; j < randomWalkPath.size(); j += 2) {
//                    matchedArrayList.add(randomWalkPath.get(j));
//                    matchedArrayList.add(randomWalkPath.get(j + 1));
//                }
//                unmatched.removeIf(s -> s.equals(randomWalkPath.get(0)));
//                unmatched.removeIf(s -> s.equals(randomWalkPath.get(randomWalkPath.size() - 1)));
//                L.remove(0);
//                L.removeIf(s -> s.equals(unmatchedNodeInTheEndOfPath));
//            }
//            System.out.println(matchedArrayList.size());
//
//
//        }
//        System.out.println(matchedArrayList.size());
//        System.out.println(matchedArrayList);
//
//
//    }

    public void solveBySimpleRandomExp(Problem problem, Neighbor neighbor, int expTimes) {
        // How many unmatched node left?
        double totalLeft = 0, minLeft = problem.getnP(), maxLeft = -1;
        for (int i = 0; i < expTimes; i++) {
            // Empty matched at begin of one exp
            matchedArrayList = new ArrayList<>();
            // Experiment starts from next line
            initL(problem.getnP());
            for (int currentNode : L) {
                if (!LHashSet.contains(currentNode)) {
                    continue;
                }
                ArrayList<Integer> currentNodeNeighbors = neighbor.getNeighbors(currentNode);
                ArrayList<Integer> unmatchedNeighbors = new ArrayList<>();
                for (int currNbr : currentNodeNeighbors) {
                    if (LHashSet.contains(currNbr)) {
                        unmatchedNeighbors.add(currNbr);
                    }
                }
                if (unmatchedNeighbors.isEmpty()) {
                    continue;
                }
                // Choose random one
                int randomIndex = (int) (Math.random() * unmatchedNeighbors.size());
                int designatedNbr = unmatchedNeighbors.get(randomIndex);
                LHashSet.remove(currentNode);
                LHashSet.remove(designatedNbr);
                matchedArrayList.add(currentNode);
                matchedArrayList.add(designatedNbr);
            }
            int countLeft = LHashSet.size();
            totalLeft += countLeft;
            if (countLeft > maxLeft) maxLeft = countLeft;
            if (countLeft < minLeft) minLeft = countLeft;
        }
        System.out.println("total=" + totalLeft + "|avg=" + totalLeft / expTimes + "|min=" + minLeft + "|max=" + maxLeft);
    }

    /*
        Difference between SimpleRandom and RandomWalk:
            RandomWalk will do randomWalk when the unmatched node's neighbors are all matched
     */
    public void solveByRandomWalkExp(Problem problem, Neighbor neighbor, int expTimes, int randomWalkPathLengthLimit) {
        // How many unmatched node left?
        double totalLeft = 0;
        int minLeft = problem.getnP(), maxLeft = -1;
        for (int i = 0; i < expTimes; i++) {
            // Empty matched at begin of one exp
            matchedHashMap = new HashMap<>();
            // Experiment starts from next line
            initL(problem.getnP());
            for (int currentNode : L) {
                if (!LHashSet.contains(currentNode)) { //LHashSet: stores unmatched nodes
                    if (LHashSet.isEmpty()) break;
                    continue;
                }
                ArrayList<Integer> currentNodeNeighbors = neighbor.getNeighbors(currentNode);
                ArrayList<Integer> unmatchedNeighbors = new ArrayList<>();
                for (int currNbr : currentNodeNeighbors) {
                    if (LHashSet.contains(currNbr)) {
                        unmatchedNeighbors.add(currNbr);
                    }
                }
                if (!unmatchedNeighbors.isEmpty()) {
                    // Pick random one, make a pair
                    int randomIndex = (int) (Math.random() * unmatchedNeighbors.size());
                    int designatedNbr = unmatchedNeighbors.get(randomIndex);
                    LHashSet.remove(currentNode);
                    LHashSet.remove(designatedNbr);
                    matchedHashMap.put(currentNode, designatedNbr);
                    matchedHashMap.put(designatedNbr, currentNode);
                    continue;
                }
                ArrayList<Integer> randomWalkPath = getRandomWalkPath(neighbor, randomWalkPathLengthLimit, currentNode);
                // randomWalkPath may be "null" if its length exceed limit. If null, do nothing
                if (randomWalkPath != null) {
                    // Remove the two end node from L
                    int firstNodeInPath = randomWalkPath.get(0);
                    int lastNodeInPath = randomWalkPath.get(randomWalkPath.size() - 1);
                    LHashSet.remove(firstNodeInPath);
                    LHashSet.remove(lastNodeInPath);
                    // Change pairs relationship in matchedHashMap
                    for (int j = 0; j < randomWalkPath.size() - 1; j++) {
                        if (j % 2 == 0) {
                            matchedHashMap.put(randomWalkPath.get(j), randomWalkPath.get(j + 1));
                            matchedHashMap.put(randomWalkPath.get(j + 1), randomWalkPath.get(j));
                        } else {
                            matchedHashMap.put(randomWalkPath.get(j), randomWalkPath.get(j - 1));
                            matchedHashMap.put(randomWalkPath.get(j - 1), randomWalkPath.get(j));
                        }
                    }
                }
            }
            int countLeft = LHashSet.size();
            totalLeft += countLeft;
            if (countLeft > maxLeft) maxLeft = countLeft;
            if (countLeft < minLeft) minLeft = countLeft;
        }
        System.out.println("total=" + totalLeft + "|avg=" + totalLeft / expTimes + "|min=" + minLeft + "|max=" + maxLeft);
    }

    private ArrayList<Integer> getRandomWalkPath(Neighbor neighbor, int randomWalkPathLengthLimit, int currentNode) {
        ArrayList<Integer> randomWalkPath = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<Integer> currentNodeNeighbors = new ArrayList<>();
        ArrayList<Integer> unmatchedNeighbors = new ArrayList<>();
        while (true) {
            randomWalkPath.add(currentNode);
            visited.add(currentNode);
            currentNodeNeighbors = neighbor.getNeighbors(currentNode);
            // Dead end check: If all neighbors are already visited along the path, then break(do nothing)
            ArrayList<Integer> unvisitedNeighbors = getUnvisitedNeighbors(currentNodeNeighbors, visited);
            if (unvisitedNeighbors.isEmpty()) {
                return null;
            }
            // There is/are unvisited neighbor(s),
            // next, check if there is an unmatched node in unvisted, if so, we found the complete path
            unmatchedNeighbors = new ArrayList<>();
            for (int currNbr : unvisitedNeighbors) {
                if (LHashSet.contains(currNbr)) {
                    unmatchedNeighbors.add(currNbr);
                }
            }
            if (!unmatchedNeighbors.isEmpty()) {
                Collections.shuffle(unmatchedNeighbors);
                randomWalkPath.add(unmatchedNeighbors.get(0));
                return randomWalkPath;
            }
            // There are unvisited but there is no unmatched, then comes to the walk
            if (randomWalkPath.size() > randomWalkPathLengthLimit) {
                return null;
            }
            // Pick random one and update currNode, do random walk
            Collections.shuffle(unvisitedNeighbors);
            int designatedNbr = unvisitedNeighbors.get(0);
            int partnerOfDesignatedNbr = matchedHashMap.get(designatedNbr);
            randomWalkPath.add(designatedNbr);
            currentNode = partnerOfDesignatedNbr;

        }
    }

    private ArrayList<Integer> getUnvisitedNeighbors(ArrayList<Integer> currentNodeNeighbors, HashSet<Integer> visited) {
        ArrayList<Integer> safeNeighbors = new ArrayList<>();
        for (int nbr : currentNodeNeighbors) {
            if (!visited.contains(nbr)) {
                safeNeighbors.add(nbr);
            }
        }
        return safeNeighbors;
    }
}
