import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Planner {

    public final Task[] taskArray;
    public final Integer[] compatibility;
    public final Double[] maxWeight;
    public final ArrayList<Task> planDynamic;
    public final ArrayList<Task> planGreedy;

    public Planner(Task[] taskArray) {

        // Should be instantiated with an Task array
        // All the properties of this class should be initialized here

        this.taskArray = taskArray;
        this.compatibility = new Integer[taskArray.length];

        Arrays.fill(compatibility, -1);

        maxWeight = new Double[taskArray.length];

        this.planDynamic = new ArrayList<>();
        this.planGreedy = new ArrayList<>();
    }

    /**
     * @param index of the {@link Task}
     * @return Returns the index of the last compatible {@link Task},
     * returns -1 if there are no compatible {@link Task}s.
     */
    public int binarySearch(int index) {
        int low = 0;
        int high = index - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (this.taskArray[mid].getFinishTime().compareTo(this.taskArray[index].getStartTime()) <= 0) {
                if (this.taskArray[mid + 1].getFinishTime().compareTo(this.taskArray[index].getStartTime()) <= 0) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }


    /**
     * {@link #compatibility} must be filled after calling this method
     */
    public void calculateCompatibility() {
        for (int i = 0; i < this.taskArray.length; i++) {
            this.compatibility[i] = binarySearch(i);
        }
    }


    /**
     * Uses {@link #taskArray} property
     * This function is for generating a plan using the dynamic programming approach.
     * @return Returns a list of planned tasks.
     */
    public ArrayList<Task> planDynamic() {
        this.calculateCompatibility();
        System.out.println("Calculating max array\n---------------------");
        this.calculateMaxWeight(this.taskArray.length - 1);
        System.out.println("\nCalculating the dynamic solution\n--------------------------------");
        this.solveDynamic(this.taskArray.length - 1);

        ArrayList<Task> planDynamic1 = new ArrayList<Task>(this.planDynamic);
        Collections.reverse(planDynamic1);
        printDynamicPlan(planDynamic1);
        return planDynamic1;
    }

    /**
     * {@link #planDynamic} must be filled after calling this method
     */
    public void solveDynamic(int i) {
        if (i == -1) {
        } else {
            System.out.println("Called findSolutionDynamic(" + i + ")");
            if (i == 0) {
                this.planDynamic.add(this.taskArray[i]);
            }
            else if (this.compatibility[i] == -1 &&
                    this.taskArray[i].getWeight() > this.maxWeight[i - 1]){
                this.planDynamic.add(this.taskArray[i]);
                solveDynamic(this.compatibility[i]);
            }
            else if (this.taskArray[i].getWeight() + this.maxWeight[this.compatibility[i]] > this.maxWeight[i - 1]){
                this.planDynamic.add(this.taskArray[i]);
                solveDynamic(this.compatibility[i]);
            }
            else {
                solveDynamic(i - 1);
            }
        }
    }

    /**
     * {@link #maxWeight} must be filled after calling this method
     */
    /* This function calculates maximum weights and prints out whether it has been called before or not  */
    public Double calculateMaxWeight(int i) {
        System.out.println("Called calculateMaxWeight(" + i + ")");
        if (i < 0) {return 0.0;}
        if (maxWeight[i] != null) {
            return maxWeight[i];
        }
        Double result = Math.max(taskArray[i].getWeight() + calculateMaxWeight(compatibility[i]),
                calculateMaxWeight(i - 1));
        maxWeight[i] = result;
        return result;
    }

    /**
     * {@link #planGreedy} must be filled after calling this method
     * Uses {@link #taskArray} property
     *
     * @return Returns a list of scheduled assignments
     */

    /*
     * This function is for generating a plan using the greedy approach.
     * */
    public ArrayList<Task> planGreedy() {
        this.planGreedy.add(this.taskArray[0]);
        for (int i=1; i < this.taskArray.length; i++){
            if (this.planGreedy.get(this.planGreedy.size() - 1).getFinishTime()
                    .compareTo(this.taskArray[i].getStartTime()) < 1){
                this.planGreedy.add(this.taskArray[i]);
            }
        }
        printGreedyPlan(planGreedy);
        return planGreedy;
    }

    public static void printDynamicPlan(ArrayList<Task> dynamicPlanArray){
        System.out.println("\nDynamic Schedule\n----------------");
        for (Task task : dynamicPlanArray){
            System.out.println("At " + task.getStartTime() + ", " + task.getName() + ".");
        }
    }
    public static void printGreedyPlan(ArrayList<Task> greedyPlanArray){
        System.out.println("\nGreedy Schedule\n---------------");
        for (Task task : greedyPlanArray){
            System.out.println("At " + task.getStartTime() + ", " + task.getName() + ".");
        }
    }
}
