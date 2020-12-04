package task01.model;

public class SomeClass implements Worker {
	@Override
    public void doWork() {
		System.out.print("Hello classLoader World");
	}
}