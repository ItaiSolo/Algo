package try1;
import java.util.ArrayList;

public class Driver {
	public static final int POPULATION_SIZE = 10;
	public static final double MUTATION_RATE = 0.25;
	public static final double CROSSOVER_RATE = 0.95;
	public static final int TOURNAMENT_SELECTION_SIZE = 3;
	public static final int NUM_OF_ELITE_SCHEDULES = 1;
	private int scheduleNumb = 0;
	private int classNum = 1;
	private Data data;

	public static void main(String[] args) {
		Driver driver = new Driver();
		driver.data = new Data(); // This would need to be fleshed out with your data
		int generationNumber = 1;

		// Print the available data
		driver.printAvailableData();

		System.out.println("> Generation # " + generationNumber);
		System.out.println(" Schedule # |                           ");
		System.out.println(" Classes [dept,class,room,instructor,meeting-time] ");
		System.out.println("            | Fitness | Conflicts");
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println();
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(driver.data);

		// Initialize population
		Population population = new Population(POPULATION_SIZE, driver.data);
		population.getSchedules().forEach(schedule -> System.out.println(" " + (driver.scheduleNumb++)
				+ " | " + schedule + " | " + String.format("%.5f",schedule.getFitness()) 
				+ " | " + schedule.getNumbOfConflicts() ));

		driver.printScheduleAsTable(population.getSchedules().get(0), generationNumber);

		driver.classNum = 1;
		while (population.getSchedules().get(0).getFitness() != 1.0) {
			System.out.println("> Generation #" + (generationNumber));
			System.out.println(" Schedule # | [dept,class,room,instructor,meeting-time] | Fitness | Conflicts");
			System.out.println("----------------------------------------------------------------------------");

			population = geneticAlgorithm.evolvePopulation(population).sortSchedulesByFitness();
			driver.scheduleNumb = 0;
			population.getSchedules().forEach(schedule -> System.out.println(" " + (driver.scheduleNumb++) + 
					" | " + schedule + " | " +
					String.format("%.5f", schedule.getFitness()) + " | " + schedule.getNumbOfConflicts()));
			
			driver.printScheduleAsTable(population.getSchedules().get(0), generationNumber);
			generationNumber++;
			driver.classNum = 1;
		}
	}

	private void printScheduleAsTable(Schedule schedule, int generation) {
		ArrayList<Class> classes = schedule.getClasses();
		System.out.println("\n |----------------------------------------------|");
		System.out.println("Class #  | Dept  | Course (number, max # of students)  | Room (Capacity)  | Instructor (Id)  | Meeting Time (Id)");
		System.out.println("|----------------------------------------------|");
		classes.forEach(x -> {
			int majorIndex = data.getDepts().indexOf(x.getDept());
			int coursesIndex = data.getCourses().indexOf(x.getCourse());
			int roomsIndex = data.getRooms().indexOf(x.getRoom());
			int instructorsIndex = data.getInstructors().indexOf(x.getInstructor());
			int meetingTimeIndex = data.getMeetingTimes().indexOf(x.getMeetingTime());
			System.out.print(" ");
			System.out.print(String.format("%1$02d", classNum));
			System.out.print(String.format("%1$4s", data.getDepts().get(majorIndex).getName()) + " | ");
			System.out.print(String.format("%1$21s", data.getCourses().get(coursesIndex).getName(),
					data.getCourses().get(coursesIndex).getNumber(),
					data.getCourses().get(coursesIndex).getMaxNumbOfStudents()) + " | ");
			System.out.print(String.format("%1$10s", data.getRooms().get(roomsIndex).getNumber(),
					data.getRooms().get(roomsIndex).getSeatingCapacity()) + " | ");
			System.out.print(String.format("%1$15s", data.getInstructors().get(instructorsIndex).getName(),
					data.getInstructors().get(instructorsIndex).getId()) + " | ");
			System.out.println(data.getMeetingTimes().get(meetingTimeIndex).getTime() +
					data.getMeetingTimes().get(meetingTimeIndex).getId() + " )");

			classNum++;
		});
		if (schedule.getFitness() == 1) System.out.println("> Solution Found in " + (generation + 1) + " generations");
		System.out.println("|----------------------------------------------|");
	}

	private void printAvailableData() {
		System.out.println("Available Departments =>");
		data.getDepts().forEach(x -> System.out.println("name: " + x.getName() + ", courses: " + x.getCourses()));

		System.out.println("Available Courses =>");
		data.getCourses().forEach(x -> System.out.println("course #: " + x.getNumber() + ", name: " + x.getName() + ", max # of students: " + x.getMaxNumbOfStudents() + ", instructors: " + x.getInstructors()));

		System.out.println("Available Rooms =>");
		data.getRooms().forEach(x -> System.out.println("room #: " + x.getNumber() + ", max seating capacity: " + x.getSeatingCapacity()));

		System.out.println("Available Instructors =>");
		data.getInstructors().forEach(x -> System.out.println("id: " + x.getId() + ", name: " + x.getName()));

		System.out.println("Available Meeting Times =>");
		data.getMeetingTimes().forEach(x -> System.out.println("id: " + x.getId() + ", Meeting Time: " + x.getTime()));

		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println();
	}
}
