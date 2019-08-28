public class TestClass{
	public static void main(String[] args) {
		
		Day8 day8 = new Day8();
		day8.parseInput("input.txt");

		long time1 = System.currentTimeMillis();
		System.out.println(day8.partOne().toString());
		double timeForFirst = (System.currentTimeMillis() - time1)/1000.;

		long time2 = System.currentTimeMillis();		
		System.out.println(day8.partTwo().toString());
		double timeForSecond = (System.currentTimeMillis() - time2)/1000.;

		System.out.println("Task 1 took: " + timeForFirst);
		System.out.println("Task 2 took: " + timeForSecond);

	}
}